package com.example.flight.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Khai báo DataStore ở cấp độ tệp
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "flight_search_preferences"
)

class UserPreferencesRepository(private val context: Context) {
    private object Keys {
        val SEARCH_QUERY = stringPreferencesKey("search_query")
    }

    val searchQuery: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[Keys.SEARCH_QUERY] ?: ""
        }

    suspend fun saveSearchQuery(query: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.SEARCH_QUERY] = query
        }
    }
}