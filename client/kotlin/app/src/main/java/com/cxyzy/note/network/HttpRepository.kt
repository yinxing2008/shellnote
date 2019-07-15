package com.cxyzy.note.network

import com.cxyzy.note.network.request.*

object HttpRepository : BaseHttpRepository() {
    suspend fun downloadNotes(req: DownloadNotesReq) = api.downloadNotesAsync(req)

    suspend fun batchUpdateNote(req: BatchUpdatedNoteReq) = api.batchUpdateNoteAsync(req)

    suspend fun recoverNoteFromTrash(req: UpdateNoteReq) = api.recoverNoteFromTrashAsync(req)

    suspend fun delNote(req: DelNoteReq) = api.delNoteAsync(req)

    suspend fun trashNote(req: TrashNoteReq) = api.trashNoteAsync(req)

    fun login(req: LoginReq) = api.login(req)
    suspend fun loginAsync(req: LoginReq) = api.loginAsync(req)

    suspend fun checkSyncState() = api.checkSyncStateAsync()
}