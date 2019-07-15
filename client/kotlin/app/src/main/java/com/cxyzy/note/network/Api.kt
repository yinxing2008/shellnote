package com.cxyzy.note.network

import com.cxyzy.note.OkHttpUrl.LOGIN_URL
import com.cxyzy.note.network.request.*
import com.cxyzy.note.network.response.*
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @POST(LOGIN_URL)
    fun login(@Body req: LoginReq): Call<BaseResp<LoginResp>>

    @POST(LOGIN_URL)
    fun loginAsync(@Body req: LoginReq): Deferred<BaseResp<LoginResp>>

    @GET("user/checkSyncState")
    fun checkSyncStateAsync(): Deferred<BaseResp<CheckSyncStateResp>>

    @POST("note/downloadNotes")
    fun downloadNotesAsync(@Body req: DownloadNotesReq): Deferred<BaseResp<DownloadNotesResp>>

    @POST("note/batchUpdate")
    fun batchUpdateNoteAsync(@Body req: BatchUpdatedNoteReq): Deferred<BaseResp<BatchUpdateNoteResp>>

    @POST("note/recoverFromTrash")
    fun recoverNoteFromTrashAsync(@Body req: UpdateNoteReq): Deferred<BaseResp<UpdateNoteResp>>

    @POST("note/del")
    fun delNoteAsync(@Body req: DelNoteReq): Deferred<BaseResp<EmptyResp>>

    @POST("note/trash")
    fun trashNoteAsync(@Body req: TrashNoteReq): Deferred<BaseResp<EmptyResp>>


}