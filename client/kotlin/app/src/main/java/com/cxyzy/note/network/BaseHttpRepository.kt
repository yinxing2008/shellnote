package com.cxyzy.note.network

import com.cxyzy.note.App
import com.cxyzy.note.BuildConfig
import com.cxyzy.note.R
import com.cxyzy.note.ext.CoroutineCallAdapterFactory
import com.cxyzy.note.network.interceptor.HttpHeaderInterceptor
import com.cxyzy.note.network.interceptor.HttpLogInterceptor
import com.cxyzy.note.utils.HttpsUtil
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


open class BaseHttpRepository {
    val api: Api by lazy {
        Retrofit.Builder()
                .baseUrl("https://192.168.186.80/notes/")
                .client(provideOkHttpClient(provideLoggingInterceptor()))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(Api::class.java)
    }

    private fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val sslParams = HttpsUtil.getSslSocketFactory(arrayOf(App.context.resources.openRawResource(R.raw.https_keystore)), null, null)
        return OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .apply {
                    addInterceptor(httpLoggingInterceptor)
                    addInterceptor(HttpLogInterceptor())
                    addInterceptor(HttpHeaderInterceptor())
                    if (BuildConfig.DEBUG) {
                        addNetworkInterceptor(StethoInterceptor())
                    }
                }.build()
    }

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY }

}