package com.cxyzy.note.network.interceptor

import com.cxyzy.note.HttpCode.TOKEN_EXPIRED
import com.cxyzy.note.ext.replaceTokenInHeader
import com.cxyzy.note.network.LoginManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenInterceptor : BaseInterceptor() {
    override fun interceptMe(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        if (isTokenExpired(response)) {
            response = loginAndReSendOriginalRequest(request, response, chain)
        }

        return response
    }

    /**
     * 重新登陆，并再次发送之前的请求
     */
    private fun loginAndReSendOriginalRequest(request: Request, originalResponse: Response, chain: Interceptor.Chain): Response {
        var response = originalResponse
        LoginManager.login(
                onSuccess = {
                    val newRequest = replaceTokenInHeader(request, it.token)
                    response?.close()
                    response = chain.proceed(newRequest)
                }
        )
        return response
    }

    private fun isTokenExpired(response: Response) = (response.code() == TOKEN_EXPIRED)
}
