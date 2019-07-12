package com.cxyzy.note.server.bean

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.*

data class User(
    @BsonId val id: String = ObjectId.get().toString(),
    var loginId: String,
    var userName: String? = null,
    var password: String,
    var email: String = "",
    var usn: Long = 0,
    var createTime: Long = Date().time,
    var fullSyncBefore: Long = Date().time
)

data class Note(
    @BsonId val id: String = ObjectId.get().toString(),
    var userId: String,
    var title: String? = null,
    var content: String? = null,
    var isTrashed: Boolean = false,
    var isDeleted: Boolean = false,
    var usn: Long = 0,
    var createTime: Long = Date().time,
    var updateTime: Long = Date().time
) {
    /**
     * default constructor for jackson deserialization
     */
    constructor() : this(id = "", userId = "")
}