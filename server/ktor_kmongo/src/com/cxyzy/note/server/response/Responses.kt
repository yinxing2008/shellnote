package com.cxyzy.note.server.response

import com.cxyzy.note.server.bean.Note

class EmptyResp

class LoginResp(
    //登陆token
    var token: String,
    //用户内部ID
    var userId: String
) {
    /**
     * default constructor for jackson deserialization
     */
    constructor() : this(token = "", userId = "")
}

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
class GetNoteResp(var note: Note? = null)

open class BaseResp<T>(
    var code: Int = 0,
    var message: String? = null,
    var data: T? = null
)