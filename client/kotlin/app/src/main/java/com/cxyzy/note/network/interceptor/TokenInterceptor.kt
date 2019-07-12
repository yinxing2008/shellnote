package com.cxyzy.note.network.interceptor

import com.cxyzy.note.HttpCode.TOKEN_EXPIRED
import com.cxyzy.note.events.RefreshEvent
import com.cxyzy.note.events.SyncEvent
import com.cxyzy.note.network.LoginManager
import okhttp3.Interceptor
import okhttp3.Response
import org.greenrobot.eventbus.EventBus
import java.io.IOException

class TokenInterceptor : Interceptor {
    private val TAG = TokenInterceptor::class.java.simpleName

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

//        if (isTokenExpired(response)) {
//            launch()
//            {
//
//            }
//            LoginManager.login(
//                    onSuccess = {
//                        EventBus.getDefault().post(SyncEvent())
//                        EventBus.getDefault().post(RefreshEvent())
//                    }
//                    , onFailure = {
//
//            })
//        }

        return response
    }

    private fun isTokenExpired(response: Response) = (response.code() == TOKEN_EXPIRED)
}
