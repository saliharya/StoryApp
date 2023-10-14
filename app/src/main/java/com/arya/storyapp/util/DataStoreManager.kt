package com.arya.storyapp.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("MyAppPreferences")

class DataStoreManager(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val KEY_TOKEN = stringPreferencesKey("token")
    }

    val tokenFlow: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_TOKEN]
        }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = token
        }
    }

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_TOKEN)
        }
    }
}
