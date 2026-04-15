package com.example.xddemo.data.repository

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.file.Files

class UserPreferencesRepositoryTest {

    @Test
    fun userHash_defaultsToEmptyString() = runTest {
        val repository = UserPreferencesRepository(createDataStore(backgroundScope))

        assertEquals("", repository.userHash.first())
    }

    @Test
    fun saveUserHash_persistsSavedValue() = runTest {
        val repository = UserPreferencesRepository(createDataStore(backgroundScope))

        repository.saveUserHash("cookie-value")

        assertEquals("cookie-value", repository.userHash.first())
    }

    private fun createDataStore(scope: CoroutineScope) = PreferenceDataStoreFactory.create(
        scope = scope,
        produceFile = {
            Files.createTempDirectory("user-preferences")
                .resolve("test.preferences_pb")
                .toFile()
        }
    )
}
