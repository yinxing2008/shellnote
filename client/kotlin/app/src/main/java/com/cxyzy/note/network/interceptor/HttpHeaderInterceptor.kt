package com.cxyzy.note.network.interceptor

import com.cxyzy.note.BEARER
import com.cxyzy.note.HTTP_HEADER_AUTH
import com.cxyzy.note.network.LoginManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * okhttp header拦截器
 */

class HttpHeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val token = LoginManager.getLoginToken()
        token?.let { request = request.newBuilder().addHeader(HTTP_HEADER_AUTH, "$BEARER $token").build() }
        return chain.proceed(request)
    }
}
