package com.example.bookself

import com.example.bookself.data.AppContainer
import android.app.Application
import com.example.bookself.data.DefaultAppContainer

class BookshelfApplication : Application() {
    val container: AppContainer by lazy {
        DefaultAppContainer()
    }
    override fun onCreate() {
        super.onCreate()
    }
}