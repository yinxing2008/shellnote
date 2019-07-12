package com.cxyzy.note.network

import com.cxyzy.note.network.request.*

object HttpRepository : BaseHttpRepository() {
    suspend fun downloadNotes(req: DownloadNotesReq) = api.downloadNotesAsync(req).await()

    suspend fun addNote(req: AddNoteReq) = api.addNoteAsync(req).await()

    suspend fun batchUpdateNote(req: BatchUpdatedNoteReq) = api.batchUpdateNoteAsync(req).await()

    suspend fun updateNote(req: UpdateNoteReq) = api.updateNoteAsync(req).await()

    suspend fun recoverNoteFromTrash(req: UpdateNoteReq) = api.recoverNoteFromTrashAsync(req).await()

    suspend fun delNote(req: DelNoteReq) = api.delNoteAsync(req).await()

    suspend fun trashNote(req: TrashNoteReq) = api.trashNoteAsync(req).await()

    suspend fun login(req: LoginReq) = api.loginAsync(req).await()

    suspend fun checkSyncState() = api.checkSyncStateAsync().await()
}