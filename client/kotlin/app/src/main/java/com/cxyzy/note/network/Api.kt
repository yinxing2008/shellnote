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
    suspend fun loginAsync(@Body req: LoginReq): BaseResp<LoginResp>

    @GET("user/checkSyncState")
    suspend fun checkSyncStateAsync(): BaseResp<CheckSyncStateResp>

    @POST("note/downloadNotes")
    suspend fun downloadNotesAsync(@Body req: DownloadNotesReq): BaseResp<DownloadNotesResp>

    @POST("note/batchUpdate")
    suspend fun batchUpdateNoteAsync(@Body req: BatchUpdatedNoteReq): BaseResp<BatchUpdateNoteResp>

    @POST("note/recoverFromTrash")
    suspend fun recoverNoteFromTrashAsync(@Body req: UpdateNoteReq): BaseResp<UpdateNoteResp>

    @POST("note/del")
    suspend fun delNoteAsync(@Body req: DelNoteReq): BaseResp<EmptyResp>

    @POST("note/trash")
    suspend fun trashNoteAsync(@Body req: TrashNoteReq): BaseResp<EmptyResp>

}