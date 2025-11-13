package com.example.castanedamariana.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class AppPreferences(private val dataStore: DataStore<Preferences>) {

    private val lastUpdateKey = stringPreferencesKey("last_update_datetime")

    val lastUpdateDateTime: Flow<String> = dataStore.data.map { preferences ->
        preferences[lastUpdateKey] ?: ""
    }

    suspend fun getLastUpdateDateTime(): String {
        return dataStore.data.map { preferences ->
            preferences[lastUpdateKey] ?: ""
        }.first()
    }

    suspend fun updateLastUpdateDateTime() {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        
        dataStore.edit { preferences ->
            preferences[lastUpdateKey] = formattedDateTime
        }
    }

    suspend fun clearLastUpdateDateTime() {
        dataStore.edit { preferences ->
            preferences.remove(lastUpdateKey)
        }
    }
}
