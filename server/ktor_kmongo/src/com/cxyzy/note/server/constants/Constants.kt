package com.cxyzy.note.server.constants


object CommonConstants {
    const val BASE_URL = "/notes"
    const val dbName = "notes"
    const val maxCountPerRequest = 50
    const val SUCCESS = 0
    //登陆token有效时间长度(单位：秒)
    const val TOKEN_VALID_SECONDS = 3600*24*10L
}

object UserConstants {
    const val invalidUsn = -1L
}