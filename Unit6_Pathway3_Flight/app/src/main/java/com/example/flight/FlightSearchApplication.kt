package com.example.flight

import android.app.Application
import com.example.flight.di.AppContainer
import com.example.flight.di.DefaultAppContainer

class FlightSearchApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
