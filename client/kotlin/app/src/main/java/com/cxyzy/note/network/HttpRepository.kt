package com.cxyzy.note.network

import com.cxyzy.note.network.request.*

object HttpRepository : BaseHttpRepository() {
    suspend fun downloadNotes(req: DownloadNotesReq) = api.downloadNotesAsync(req).await()

    suspend fun batchUpdateNote(req: BatchUpdatedNoteReq) = api.batchUpdateNoteAsync(req).await()

    suspend fun recoverNoteFromTrash(req: UpdateNoteReq) = api.recoverNoteFromTrashAsync(req).await()

    suspend fun delNote(req: DelNoteReq) = api.delNoteAsync(req).await()

    suspend fun trashNote(req: TrashNoteReq) = api.trashNoteAsync(req).await()

    fun login(req: LoginReq) = api.login(req)
    suspend fun loginAsync(req: LoginReq) = api.loginAsync(req).await()

    suspend fun checkSyncState() = api.checkSyncStateAsync().await()
}