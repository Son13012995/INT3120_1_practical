package com.example.bookself

import AppContainer
import DefaultAppContainer
import android.app.Application

class BookshelfApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}