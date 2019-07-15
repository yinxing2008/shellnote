package com.cxyzy.note.network.interceptor

import com.cxyzy.note.ext.addTokenInHeader
import com.cxyzy.note.network.LoginManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * okhttp header拦截器
 */

class HttpHeaderInterceptor : BaseInterceptor() {

    override fun interceptMe(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val token = LoginManager.getLoginToken()
        token?.let { request = addTokenInHeader(request, it) }
        return chain.proceed(request)
    }
}
