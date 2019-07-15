package com.cxyzy.note

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.cxyzy.note.events.BaseEvent
import com.cxyzy.note.events.SyncEvent
import com.cxyzy.note.utils.logger.initLogger
import com.facebook.stetho.Stetho
import com.facebook.stetho.Stetho.newInitializerBuilder
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        initLogger(BuildConfig.DEBUG)
        startKoinMoudles()
        EventBus.getDefault().register(this)
        syncWithServer()
        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                    newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                            .build())
        }
    }


    private fun startKoinMoudles() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }

    private fun syncWithServer() {
        val request = OneTimeWorkRequestBuilder<DataSyncWorker>().build()
        WorkManager.getInstance().enqueue(request)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: BaseEvent) {
        if (event is SyncEvent) {
            syncWithServer()
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

}