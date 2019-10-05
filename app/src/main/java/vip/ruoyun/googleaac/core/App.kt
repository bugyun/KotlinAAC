package vip.ruoyun.googleaac.core

import android.app.Application
import android.content.Context

class App : Application() {

    private val application: Application by lazy {
        this
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }
}
