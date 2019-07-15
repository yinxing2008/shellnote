package com.cxyzy.note.ext

import com.cxyzy.note.*
import com.cxyzy.note.network.interceptor.HttpHeaderInterceptor
import com.cxyzy.note.network.interceptor.HttpLogInterceptor
import com.cxyzy.note.network.interceptor.TokenInterceptor
import com.cxyzy.note.utils.HttpsUtil
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

fun provideOkHttpClient(): OkHttpClient {
    val sslParams = HttpsUtil.getSslSocketFactory(arrayOf(App.context.resources.openRawResource(R.raw.https_keystore)), null, null)
    return OkHttpClient.Builder()
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
            .apply {
                addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                addInterceptor(HttpLogInterceptor())
                addInterceptor(HttpHeaderInterceptor())
                addInterceptor(TokenInterceptor())
                if (BuildConfig.DEBUG) {
                    addNetworkInterceptor(StethoInterceptor())
                }
            }.build()
}

fun addTokenInHeader(request: Request, token: String): Request {
    return request.newBuilder()
            .addHeader(HTTP_HEADER_AUTH, "$BEARER $token")
            .build()
}

fun replaceTokenInHeader(request: Request, token: String): Request {
    return request.newBuilder()
            .removeHeader(HTTP_HEADER_AUTH)
            .addHeader(HTTP_HEADER_AUTH, "$BEARER $token")
            .build()
}