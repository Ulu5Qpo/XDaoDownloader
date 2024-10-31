package com.example.xddemo.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
){
    private companion object {
        val USER_HASH = stringPreferencesKey("userhash")
    }

    val userHash: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e("UserPreferencesRepo", "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[USER_HASH] ?: ""
        }

    suspend fun saveUserHash(userhash: String) {
        dataStore.edit { preferences ->
            preferences[USER_HASH] = userhash
        }
        Log.d("saveUserHash", "userhash:$userhash")
    }
}