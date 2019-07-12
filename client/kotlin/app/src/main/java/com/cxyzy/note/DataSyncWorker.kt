package com.cxyzy.note

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cxyzy.note.db.AppDatabase
import com.cxyzy.note.db.bean.Note
import com.cxyzy.note.db.dao.NoteDao
import com.cxyzy.note.ext.KoinInject.getFromKoin
import com.cxyzy.note.network.HttpRepository
import com.cxyzy.note.network.HttpRepository.downloadNotes
import com.cxyzy.note.network.LoginManager
import com.cxyzy.note.network.LoginManager.shouldRefreshToken
import com.cxyzy.note.network.request.BatchUpdatedNoteReq
import com.cxyzy.note.network.request.DownloadNotesReq
import com.cxyzy.note.network.request.LoginReq
import com.cxyzy.note.network.request.SimpleNoteReq
import com.cxyzy.note.network.response.BaseResp
import com.cxyzy.note.network.response.CheckSyncStateResp
import com.cxyzy.note.network.response.DownloadNotesResp
import com.cxyzy.note.utils.spUtils.UserSPUtil
import com.cxyzy.note.utils.spUtils.UserSPUtil.getLocalLastFullSyncDateFromSP
import com.cxyzy.note.utils.spUtils.UserSPUtil.getMaxLocalNoteUsnFromSP
import com.cxyzy.note.utils.spUtils.UserSPUtil.getServerFullSyncBeforeFromSP
import com.cxyzy.note.utils.spUtils.UserSPUtil.resetMaxNoteUsnInSP
import com.cxyzy.note.utils.spUtils.UserSPUtil.saveLocalLastFullSyncDateInSP
import com.cxyzy.note.utils.spUtils.UserSPUtil.saveMaxNoteUsnInSP
import com.cxyzy.note.utils.spUtils.UserSPUtil.saveServerFullSyncBeforeInSP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import java.util.*

class DataSyncWorker(
        context: Context,
        workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override val coroutineContext = Dispatchers.IO

    override suspend fun doWork(): Result = coroutineScope {
        val database = getFromKoin<AppDatabase>()
        val noteDao = database.noteDao()
        if (LoginManager.isLoggedIn()) {
            if (shouldRefreshToken()) {
                val loginId = UserSPUtil.getLoginIdFromSP()
                val password = UserSPUtil.getLoginPassFromSP()
                if (loginId.isNullOrEmpty() || password.isNullOrEmpty()) {
                    return@coroutineScope Result.failure()
                }
                val resp = HttpRepository.login(LoginReq(loginId, password))
                if (resp.isSuccess()) {
                    resp.data?.let {
                        LoginManager.saveLoginInfo(loginId, password, it)
                        syncData(noteDao)
                    }
                }
            }
            syncData(noteDao)
        }

        Result.success()
    }

    private suspend fun syncData(noteDao: NoteDao) {
        val resp = HttpRepository.checkSyncState()
        if (resp.isSuccess()) {
            resp.data?.let {
                syncData(it, noteDao)
            }
        }
    }

    private suspend fun syncData(it: CheckSyncStateResp, noteDao: NoteDao) {
        saveServerFullSyncBeforeInSP(it.fullSyncBefore)
        if (needFullSync() || it.userMaxUsn > getMaxLocalNoteUsnFromSP()) {
            if (needFullSync()) {
                clearLocalDataBeforeFullSync(noteDao)
            }
            downloadNotes(noteDao)
        }
        uploadNotes(noteDao)
    }

    /**
     * 如果本地记录的全量同步时间早于服务器上要求的全量同步时间，
     * 则删除本地已经同步过的记录，重置本地记录的最大usn(从头开始下载数据),
     * 更新本地记录的全量同步时间为服务器全量同步时间。
     */
    private fun clearLocalDataBeforeFullSync(noteDao: NoteDao) {
        delLocalSyncedNotes(noteDao)
        resetMaxNoteUsnInSP()
        saveLocalLastFullSyncDateInSP(getServerFullSyncBeforeFromSP())
    }

    /**
     * 本地记录的全量同步时间是否早于服务器上的全量同步时间
     */
    private fun needFullSync(): Boolean = getServerFullSyncBeforeFromSP() > getLocalLastFullSyncDateFromSP()

    /**
     * 删除本地已经同步的记录，即没有dirty标志的记录
     */
    private fun delLocalSyncedNotes(noteDao: NoteDao) {
        noteDao.delUnDirtyNotes()
    }

    private suspend fun downloadNotes(noteDao: NoteDao) {
        var afterUsn = getMaxLocalNoteUsnFromSP()
        val maxCount = MAX_DOWNLOAD_COUNT_PER_REQUEST
        var req = DownloadNotesReq(afterUsn = afterUsn, maxCount = maxCount)
        var resp = downloadNotes(req)

        while (isResponseNotesNoteEmpty(resp)) {
            afterUsn = processDownloadedNotes(resp, noteDao, afterUsn)
            if (hasMoreToDownload(resp, maxCount)) {
                req = DownloadNotesReq(afterUsn = afterUsn, maxCount = maxCount)
                resp = downloadNotes(req)
            } else {
                break
            }
        }
    }

    private fun isResponseNotesNoteEmpty(resp: BaseResp<DownloadNotesResp>) =
            resp.data?.notes != null && resp.data?.notes?.run { isNotEmpty() } == true

    private fun processDownloadedNotes(resp: BaseResp<DownloadNotesResp>, noteDao: NoteDao, originalAfterUsn: Long): Long {
        var newAfterUsn = originalAfterUsn
        resp.data?.notes?.forEach { serverNote ->
            val serverNoteUsn = serverNote.usn ?: 0L
            val localNote = noteDao.getNote(serverNote.id)
            if (localNote != null) {
                if (isLocalConflictWithServer(localNote, serverNoteUsn)) {
                    copyLocalNoteAndAddServerNote(localNote, noteDao, serverNote)
                } else if (hasUpdatedOnServer(localNote, serverNoteUsn)) {
                    noteDao.update(serverNote)
                }
            } else {
                noteDao.add(serverNote)
            }
        }

        resp.data?.notes?.let {
            val serverMaxUsn = getMaxUsn(it)
            saveMaxNoteUsnInSP(serverMaxUsn)
            newAfterUsn = serverMaxUsn
        }

        return newAfterUsn
    }

    /**
     * 如果本次获取的数据总量小于单次最大下载数，则表示没有更多数据了。
     */
    private fun hasMoreToDownload(resp: BaseResp<DownloadNotesResp>, maxCount: Int): Boolean {
        var result = true
        resp.data?.notes?.let {
            if (it.size < maxCount) {
                result = false
            }
        }
        return result
    }

    private suspend fun uploadNotes(noteDao: NoteDao) {
        val toAddNoteList = getToAddList(noteDao)
        val toUpdateNoteList = getToUpdateList(noteDao)
        if (toAddNoteList.isEmpty() && toUpdateNoteList.isEmpty()) {
            return
        }
        val resp = HttpRepository.batchUpdateNote(BatchUpdatedNoteReq(toAddNoteList, toUpdateNoteList))
        resp.data?.notes?.forEach { note ->
            note.isDirty = false
        }

        resp.data?.notes?.let {
            noteDao.update(it)
            val maxUsn = getMaxUsn(it)
            if (isLocalIdenticalWithServer(toAddNoteList, toUpdateNoteList, maxUsn)) {
                saveMaxNoteUsnInSP(maxUsn)
            }
        }
    }

    /**
     * 上传后，本地和服务器数据是否一致。如果没有其他客户端在同步操作用户的数据，本地和服务器就是一致的，否则就会出现不一致。
     * 如果一致的话，上传完数据，需要更新本地保存的最大usn。
     */
    private fun isLocalIdenticalWithServer(toAddNoteList: MutableList<SimpleNoteReq>,
                                           toUpdateNoteList: MutableList<SimpleNoteReq>,
                                           serverMaxUsn: Long): Boolean {
        var result = false
        val toChangeCount = (toAddNoteList.size + toUpdateNoteList.size).toLong()
        val usnDiff = serverMaxUsn - getMaxLocalNoteUsnFromSP()
        if (toChangeCount == usnDiff) {
            result = true
        }
        return result
    }

    private fun getMaxUsn(notes: List<Note>?): Long {
        var maxUsn = 0L
        notes?.forEach { note ->
            val noteUsn = note.usn ?: 0L
            if (noteUsn > maxUsn) {
                maxUsn = noteUsn
            }
        }
        return maxUsn
    }

    private fun getToUpdateList(noteDao: NoteDao): MutableList<SimpleNoteReq> {
        val toUpdateNotes = noteDao.getDirtyNotesWithUsn(MAX_UPLOAD_COUNT_PER_REQUEST)
        val toUpdateNoteList = mutableListOf<SimpleNoteReq>()
        for (note in toUpdateNotes) {
            toUpdateNoteList.add(SimpleNoteReq(note.id, note.title, note.content))
        }
        return toUpdateNoteList
    }

    private fun getToAddList(noteDao: NoteDao): MutableList<SimpleNoteReq> {
        val toAddNotes = noteDao.getDirtyNotesWithoutUsn(MAX_UPLOAD_COUNT_PER_REQUEST)
        val toAddNoteList = mutableListOf<SimpleNoteReq>()
        for (note in toAddNotes) {
            toAddNoteList.add(SimpleNoteReq(note.id, note.title, note.content))
        }
        return toAddNoteList
    }

    /**
     * 判断本地记录是否和服务器记录存在冲突。
     * 本地笔记有dirty标志，服务器上的usn比本地大(表明服务器上被其他客户端修改过)。
     */
    private fun isLocalConflictWithServer(localNote: Note, serverNoteUsn: Long) =
            localNote.isDirty && serverNoteUsn > (localNote.usn ?: 0L)

    /**
     * 判断服务器记录相对本地记录做过修改。
     */
    private fun hasUpdatedOnServer(localNote: Note, serverNoteUsn: Long) =
            !localNote.isDirty && serverNoteUsn > (localNote.usn ?: 0L)

    /**
     * 当出现笔记冲突时：
     * 复制本地记录，修改id为一个新的id；
     * 删除本地记录；
     * 将服务器记录添加到本地记录中。
     */
    private fun copyLocalNoteAndAddServerNote(localNote: Note, noteDao: NoteDao, serverNote: Note) {
        val copiedNote = localNote.copy()
        copiedNote.id = UUID.randomUUID().toString()
        noteDao.add(copiedNote)
        noteDao.del(localNote)
        noteDao.add(serverNote)
    }
}