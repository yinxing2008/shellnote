package com.cxyzy.note.network

import com.cxyzy.note.REFRESH_TOKEN_THRESHOLD
import com.cxyzy.note.network.request.LoginReq
import com.cxyzy.note.network.response.BaseResp
import com.cxyzy.note.network.response.EmptyResp
import com.cxyzy.note.network.response.LoginResp
import com.cxyzy.note.utils.spUtils.UserSPUtil
import com.cxyzy.note.utils.spUtils.UserSPUtil.getLoginRespFromSP
import com.cxyzy.note.utils.spUtils.UserSPUtil.getLoginTimeFromSP
import java.util.*

object LoginManager {
    fun isLoggedIn(): Boolean {
        return getLoginToken() != null
    }

    fun getLoginToken() = getLoginRespFromSP()?.token
    fun getTokenValidSeconds() = getLoginRespFromSP()?.tokenValidSeconds
    fun getUserId() = getLoginRespFromSP()?.userId

    /**
     * 当token剩余有效期小于参数REFRESH_TOKEN_THRESHOLD指定比例时，重新登陆获取token
     */
    fun shouldRefreshToken(): Boolean {
        var result = false
        val resp = getLoginRespFromSP()
        resp?.let {
            val timePassedInSeconds = (Date().time - getLoginTimeFromSP()) / 1000
            if (timePassedInSeconds > (1 - REFRESH_TOKEN_THRESHOLD) * resp.tokenValidSeconds) {
                result = true
            }
        }
        return result
    }

    fun saveLoginInfo(loginId: String, password: String, loginResp: LoginResp) {
        UserSPUtil.saveUserIdInSP(loginResp.userId)
        UserSPUtil.saveLoginIdInSP(loginId)
        UserSPUtil.saveLoginPassInSP(password)
        UserSPUtil.saveLoginRespInSP(loginResp)
    }

    suspend fun login(loginId: String = UserSPUtil.getLoginIdFromSP() ?: "",
                      password: String = UserSPUtil.getLoginPassFromSP() ?: "",
                      onSuccess: (() -> Unit)? = null,
                      onFailure: ((resp: BaseResp<EmptyResp>) -> Unit)? = null) {
        if (loginId.isEmpty() || password.isEmpty()) {
            return
        }
        val resp = HttpRepository.login(LoginReq(loginId, password))
        if (resp.isSuccess()) {
            resp.data?.let {
                saveLoginInfo(loginId, password, it)
                onSuccess?.invoke()
            }
            onFailure?.invoke(resp.getEmptyResp())
        } else {
            onFailure?.invoke(resp.getEmptyResp())
        }
    }
}