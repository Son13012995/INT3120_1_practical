package com.example.amphibians

import android.app.Application
import com.example.amphibians.data.AppContainer
import com.example.amphibians.data.DefaultAppContainer

class AmphibianApplication : Application() {
    /** AppContainer instance được dùng bởi toàn bộ app */
    val container: AppContainer by lazy {
        DefaultAppContainer()
    }
    override fun onCreate() {
        super.onCreate()
    }
}