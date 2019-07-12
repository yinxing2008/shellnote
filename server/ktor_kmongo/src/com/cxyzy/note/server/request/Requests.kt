package com.cxyzy.note.server.request

data class RegisterReq(
    val loginId: String,
    val password: String
)

data class LoginReq(
    val loginId: String,
    val password: String
)

data class SimpleNoteReq(
    val noteId: String,
    val title: String?,
    val content: String?
)

data class AddNoteReq(val list: List<SimpleNoteReq>)
data class UpdateNoteReq(val list: List<SimpleNoteReq>)
data class BatchUpdatedNoteReq(
    val toAddList: List<SimpleNoteReq>,
    val toUpdateList: List<SimpleNoteReq>
)

data class DownloadNotesReq(
    val afterUsn: Long,
    val maxCount: Int? = null
)

data class GetNoteReq(val noteId: String)
data class DelNoteReq(val noteId: String)
data class TrashNoteReq(val noteId: String)