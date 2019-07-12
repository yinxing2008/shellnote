package com.cxyzy.note.server.service

import com.cxyzy.note.server.Auth
import com.cxyzy.note.server.bean.User
import com.cxyzy.note.server.constants.ErrorInfo.INVALID_LOGIN_ID_OR_PWD
import com.cxyzy.note.server.constants.ErrorInfo.INVALID_LOGIN_ID_OR_PWD_INFO
import com.cxyzy.note.server.constants.ErrorInfo.USER_EXISTS
import com.cxyzy.note.server.constants.ErrorInfo.USER_EXISTS_INFO
import com.cxyzy.note.server.dao.UserDao
import com.cxyzy.note.server.ext.inject
import com.cxyzy.note.server.response.BaseResp
import com.cxyzy.note.server.response.CheckSyncStateResp
import com.cxyzy.note.server.response.EmptyResp
import com.cxyzy.note.server.response.LoginResp

class UserService : BaseService() {

    private val userDao: UserDao by inject()

    suspend fun login(loginId: String, password: String): BaseResp<LoginResp> {
        val user = userDao.getUserByLoginId(loginId)
        val result = BaseResp<LoginResp>()
        if (user != null && user.password == password) {
            result.data = LoginResp(token = Auth.makeToken(user.id), userId = user.id)
        } else {
            result.code = INVALID_LOGIN_ID_OR_PWD
            result.message = INVALID_LOGIN_ID_OR_PWD_INFO
        }
        return result
    }

    suspend fun checkSyncState(userId: String): BaseResp<CheckSyncStateResp> {
        val user = userDao.getUserByUserId(userId)
        val result = BaseResp<CheckSyncStateResp>()
        user?.let { result.data = CheckSyncStateResp(it.usn, it.fullSyncBefore) }
        return result
    }

    suspend fun register(user: User): BaseResp<EmptyResp> {
        val result = BaseResp<EmptyResp>()
        if (!userDao.isExistByLoginId(user.loginId)) {
            userDao.add(user)
        } else {
            result.code = USER_EXISTS
            result.message = USER_EXISTS_INFO
        }
        return result
    }

}