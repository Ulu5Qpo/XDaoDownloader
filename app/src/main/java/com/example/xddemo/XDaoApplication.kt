package com.example.xddemo

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.xddemo.data.AppContainer
import com.example.xddemo.data.DefaultAppContainer
import com.example.xddemo.data.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "cookie"
)

class XDaoApplication : Application() {
    lateinit var container: AppContainer
    lateinit var userPreferencesRepository: UserPreferencesRepository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        userPreferencesRepository = UserPreferencesRepository(dataStore)

        applicationScope.launch {
            userPreferencesRepository.userHash.collect { cookie ->
                container = DefaultAppContainer(this@XDaoApplication, cookie)
                Log.d("DataStoreDebug", "cookie:$cookie")
            }
        }
    }

}