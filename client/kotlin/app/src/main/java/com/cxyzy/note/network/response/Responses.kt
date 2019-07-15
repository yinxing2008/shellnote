package com.cxyzy.note.network.response

import com.cxyzy.note.RESP_SUCCESS
import com.cxyzy.note.db.bean.Note

class EmptyResp

class LoginResp(
        //登陆token
        var token: String,
        //用户内部ID
        var userId: String
)

class CheckSyncStateResp(
        //用户最大usn
        var userMaxUsn: Long,
        //如果客户端记录的fullSyncBefore早于服务器端值，则进行全量同步
        var fullSyncBefore: Long
)

class AddNoteResp(var notes: List<Note>? = null)
class UpdateNoteResp(var notes: List<Note>? = null)
class BatchUpdateNoteResp(var notes: List<Note>? = null)
class DownloadNotesResp(var notes: List<Note>? = null)

open class BaseResp<T>(
        var code: Int = RESP_SUCCESS,
        var message: String? = null,
        var data: T? = null
) {
    fun getEmptyResp() = BaseResp<EmptyResp>(code = code, message = message)
    fun isSuccess(): Boolean = (code == RESP_SUCCESS)
}