package com.cxyzy.note.network

import com.cxyzy.note.ENCRYPT_PASSWORD
import com.cxyzy.note.network.request.LoginReq
import com.cxyzy.note.network.response.BaseResp
import com.cxyzy.note.network.response.EmptyResp
import com.cxyzy.note.network.response.LoginResp
import com.cxyzy.note.utils.spUtils.UserSPUtil
import com.cxyzy.note.utils.spUtils.UserSPUtil.getLoginIdFromSP
import com.cxyzy.note.utils.spUtils.UserSPUtil.getLoginPassFromSP
import com.cxyzy.note.utils.spUtils.UserSPUtil.getLoginRespFromSP
import com.cxyzy.utils.EncryptUtils


object LoginManager {
    fun isLoggedIn(): Boolean {
        return getLoginToken() != null
    }

    fun getLoginToken() = getLoginRespFromSP()?.token

    private fun saveLoginInfo(loginId: String, password: String, loginResp: LoginResp) {
        UserSPUtil.saveUserIdInSP(loginResp.userId)
        UserSPUtil.saveLoginIdInSP(loginId)
        UserSPUtil.saveLoginPassInSP(EncryptUtils.encrypt(ENCRYPT_PASSWORD, password))
        UserSPUtil.saveLoginRespInSP(loginResp)
    }

    fun login(onSuccess: ((resp: LoginResp) -> Unit)? = null,
              onFailure: ((resp: BaseResp<EmptyResp>) -> Unit)? = null) {
        val loginId = getLoginIdFromSP()
        var password = ""
        try {
            password = EncryptUtils.decrypt(ENCRYPT_PASSWORD, getLoginPassFromSP() ?: "")
        } catch (e: Exception) {
        }
        if (loginId.isNullOrEmpty() || password.isNullOrEmpty()) {
            return
        }

        val call = HttpRepository.login(LoginReq(loginId, password))
        val response = call.execute()

        response.body()?.data?.let {
            saveLoginInfo(loginId, password, it)
            onSuccess?.invoke(it)
            return
        }
        onFailure?.invoke(BaseResp(message = response.body()?.message))
    }

    suspend fun loginAsync(loginId: String = getLoginIdFromSP() ?: "",
                           password: String = EncryptUtils.decrypt(ENCRYPT_PASSWORD, getLoginPassFromSP()
                                   ?: ""),
                           onSuccess: ((resp: LoginResp) -> Unit)? = null,
                           onFailure: ((resp: BaseResp<EmptyResp>) -> Unit)? = null) {
        if (loginId.isEmpty() || password.isEmpty()) {
            return
        }
        val resp = HttpRepository.loginAsync(LoginReq(loginId, password))
        if (resp.isSuccess()) {
            resp.data?.let {
                saveLoginInfo(loginId, password, it)
                onSuccess?.invoke(it)
                return
            }
            onFailure?.invoke(resp.getEmptyResp())
        } else {
            onFailure?.invoke(resp.getEmptyResp())
        }
    }

}