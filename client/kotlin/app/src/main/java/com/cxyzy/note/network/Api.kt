package com.cxyzy.note.network

import com.cxyzy.note.network.request.*
import com.cxyzy.note.network.response.*
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @POST("user/login")
    fun loginAsync(@Body req: LoginReq): Deferred<BaseResp<LoginResp>>

    @GET("user/checkSyncState")
    fun checkSyncStateAsync(): Deferred<BaseResp<CheckSyncStateResp>>

    @POST("note/downloadNotes")
    fun downloadNotesAsync(@Body req: DownloadNotesReq): Deferred<BaseResp<DownloadNotesResp>>

    @POST("note/add_note_float")
    fun addNoteAsync(@Body req: AddNoteReq): Deferred<BaseResp<AddNoteResp>>

    @POST("note/update")
    fun updateNoteAsync(@Body req: UpdateNoteReq): Deferred<BaseResp<UpdateNoteResp>>

    @POST("note/batchUpdate")
    fun batchUpdateNoteAsync(@Body req: BatchUpdatedNoteReq): Deferred<BaseResp<BatchUpdateNoteResp>>

    @POST("note/recoverFromTrash")
    fun recoverNoteFromTrashAsync(@Body req: UpdateNoteReq): Deferred<BaseResp<UpdateNoteResp>>

    @POST("note/del")
    fun delNoteAsync(@Body req: DelNoteReq): Deferred<BaseResp<EmptyResp>>

    @POST("note/trash")
    fun trashNoteAsync(@Body req: TrashNoteReq): Deferred<BaseResp<EmptyResp>>


}