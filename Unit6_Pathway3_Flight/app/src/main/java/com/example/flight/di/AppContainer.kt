package com.example.flight.di

import android.content.Context
import com.example.flight.data.AppDatabase
import com.example.flight.data.FlightRepository
import com.example.flight.data.OfflineFlightRepository
import com.example.flight.data.UserPreferencesRepository

interface AppContainer {
    val flightRepository: FlightRepository
    val userPreferencesRepository: UserPreferencesRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    override val flightRepository: FlightRepository by lazy {
        OfflineFlightRepository(AppDatabase.getDatabase(context).flightDao())
    }

    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context)
    }
}
