package com.cxyzy.note.server.service

import com.cxyzy.note.server.bean.Note
import com.cxyzy.note.server.dao.NoteDao
import com.cxyzy.note.server.dao.UserDao
import com.cxyzy.note.server.ext.inject
import com.cxyzy.note.server.response.*
import java.util.*

class NoteService : BaseService() {

    private val noteDao: NoteDao by inject()
    private val userDao: UserDao by inject()

    suspend fun getNotes(userId: String, afterUsn: Long, maxCount: Int): BaseResp<DownloadNotesResp> {
        val result = BaseResp<DownloadNotesResp>()
        result.data = DownloadNotesResp(noteDao.getNotes(userId, afterUsn, maxCount))
        return result
    }

    suspend fun getNote(noteId: String): BaseResp<GetNoteResp> {
        val result = BaseResp<GetNoteResp>()
        result.data = GetNoteResp(noteDao.getNote(noteId))
        return result
    }

    suspend fun addNote(userId: String, notes: List<Note>): BaseResp<AddNoteResp> {
        addNote(notes, userId)
        val result = BaseResp<AddNoteResp>()
        result.data = AddNoteResp(notes)
        return result
    }

    private suspend fun addNote(notes: List<Note>, userId: String) {
        for (note in notes) {
            val usn = userDao.increaseUsn(userId)
            note.usn = usn
        }

        noteDao.add(notes)
    }

    suspend fun updateNote(userId: String, notes: List<Note>): BaseResp<UpdateNoteResp> {
        updateNotes(notes, userId)

        val result = BaseResp<UpdateNoteResp>()
        result.data = UpdateNoteResp(notes)
        return result
    }

    suspend fun batchUpdate(
        userId: String,
        toAddNotes: List<Note>,
        toUpdateNotes: List<Note>
    ): BaseResp<BatchUpdateNoteResp> {
        if (toAddNotes.isNotEmpty()) {
            addNote(toAddNotes, userId)
        }

        if (toUpdateNotes.isNotEmpty()) {
            updateNotes(toUpdateNotes, userId)
        }

        val mergedNoteList: List<Note> = toAddNotes + toUpdateNotes
        val result = BaseResp<BatchUpdateNoteResp>()
        result.data = BatchUpdateNoteResp(mergedNoteList)
        return result
    }

    private suspend fun updateNotes(notes: List<Note>, userId: String) {
        for (note in notes) {
            updateNote(userId, note)
        }
    }

    suspend fun trashNote(userId: String, noteId: String): BaseResp<EmptyResp> {
        val note = noteDao.getNote(noteId)
        note?.let {
            it.isTrashed = true
            updateNote(userId, it)
        }
        return BaseResp()
    }

    suspend fun recoverNoteFromTrash(userId: String, noteId: String): BaseResp<UpdateNoteResp> {
        val note = noteDao.getNote(noteId)
        note?.let {
            it.isTrashed = false
            it.isDeleted = false
            updateNote(userId, it)
        }
        return BaseResp()
    }

    suspend fun delNote(userId: String, noteId: String): BaseResp<EmptyResp> {
        val note = noteDao.getNote(noteId)
        note?.let {
            it.isDeleted = true
            updateNote(userId, it)
        }
        return BaseResp()
    }


    private suspend fun updateNote(userId: String, note: Note) {
        val usn = userDao.increaseUsn(userId)
        note.usn = usn
        note.updateTime = Date().time
        noteDao.update(note.id, note)
    }

}