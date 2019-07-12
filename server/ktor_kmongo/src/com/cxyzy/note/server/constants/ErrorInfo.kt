package com.cxyzy.note.server.constants


object ErrorInfo {
    /**
     * 用户名或者密码不正确
     */
    const val INVALID_LOGIN_ID_OR_PWD = 100
    const val INVALID_LOGIN_ID_OR_PWD_INFO = "invalid loginId or password"
    /**
     * 用户已存在
     */
    const val USER_EXISTS = 101
    const val USER_EXISTS_INFO = "user already exists"
}