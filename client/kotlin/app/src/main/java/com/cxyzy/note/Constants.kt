package com.cxyzy.note

const val BEARER = "Bearer"
const val HTTP_HEADER_AUTH = "Authorization"
const val RESP_SUCCESS = 0
//上传接口单次最大上传数据量
const val MAX_UPLOAD_COUNT_PER_REQUEST = 10
//下载接口单次最大下载数据量，应该和服务器端配置保持一致
const val MAX_DOWNLOAD_COUNT_PER_REQUEST = 50
const val DB_FIELD_TRUE = 1
const val DB_FIELD_FALSE = 0


const val USER_SP_FILE_PREFIX = "user_sp_"
const val USER_DB_NAME_PREFIX = "user_note_"
//用户登陆前记录相关信息的SP文件
const val SHARED_SP_FILE = "shared_sp_file"

//当token剩余有效期小于下面参数指定比例时，重新登陆获取token
const val REFRESH_TOKEN_THRESHOLD = 0.2


object HttpCode {
    //token过期
    const val TOKEN_EXPIRED = 401
}


object PrefKeys {
    const val USER_ID_BEFORE_LOGIN = "USER_ID_BEFORE_LOGIN"
    const val MAX_LOCAL_NOTE_USN = "MAX_LOCAL_NOTE_USN"
    const val LOGIN_ID = "LOGIN_ID"
    const val LOGIN_RESP = "LOGIN_RESP"
    const val LOGIN_TIME = "LOGIN_TIME"
    const val USER_ID = "USER_ID"
    const val LOGIN_PASS = "LOGIN_PASS"
    const val SERVER_FULL_SYNC_BEFORE = "SERVER_FULL_SYNC_BEFORE"
    const val LOCAL_LAST_FULL_SYNC_DATE = "LOCAL_LAST_FULL_SYNC_DATE"
}

object ExtraKey {
    const val KEY_NOTE = "note"
}