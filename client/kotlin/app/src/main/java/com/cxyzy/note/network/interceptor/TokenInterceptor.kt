package com.cxyzy.note.network.interceptor

import com.cxyzy.note.HttpCode.TOKEN_EXPIRED
import com.cxyzy.note.ext.replaceTokenInHeader
import com.cxyzy.note.network.LoginManager
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : BaseInterceptor() {
    override fun interceptMe(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        try {
            response = chain.proceed(request)
            if (isTokenExpired(response!!)) {
                LoginManager.login(
                        onSuccess = {
                            val newRequest = replaceTokenInHeader(request, it.token)
                            response?.close()
                            response = chain.proceed(newRequest)
                        }
                )
            }
        } catch (e: Exception) {
        }

        return response!!
    }

    private fun isTokenExpired(response: Response) = (response.code() == TOKEN_EXPIRED)
}
