package com.cxyzy.note.network

import com.cxyzy.note.OkHttpUrl.BASE_URL
import com.cxyzy.note.ext.KoinInject
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BaseHttpRepository {
    val api: Api by lazy {
        val okHttpClient = KoinInject.getFromKoin<OkHttpClient>()
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(Api::class.java)
    }

}