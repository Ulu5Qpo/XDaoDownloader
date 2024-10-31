package com.example.xddemo.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ReplyPositionPreferences {
    private const val DATASTORE_NAME = "reply_position_prefs"
    private val Context.replyPositionDataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

    private fun THREAD_POSITION(threadId: Int) = intPreferencesKey("thread_position_$threadId")

    suspend fun savePosition(context: Context, threadId: Int, position: Int) {
        context.replyPositionDataStore.edit { preferences ->
            preferences[THREAD_POSITION(threadId)] = position
        }
    }

    fun getPosition(context: Context, threadId: Int): Flow<Int?> {
        return context.replyPositionDataStore.data
            .map { preferences -> preferences[THREAD_POSITION(threadId)] }
    }
}
