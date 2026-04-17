package com.example.xddemo

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import com.example.xddemo.data.AppContainer
import com.example.xddemo.data.DefaultAppContainer
import com.example.xddemo.data.repository.UserPreferencesRepository
import com.example.xddemo.network.AddCookieInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "cookie"
)

class XDaoApplication : Application(), SingletonImageLoader.Factory {
    lateinit var container: AppContainer
    lateinit var userPreferencesRepository: UserPreferencesRepository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        userPreferencesRepository = UserPreferencesRepository(dataStore)

        val cookieInterceptor = AddCookieInterceptor()
        container = DefaultAppContainer(this, cookieInterceptor)

        applicationScope.launch {
            userPreferencesRepository.userHash.collect { cookie ->
                cookieInterceptor.cookie = cookie
                Log.d("DataStoreDebug", "cookie:$cookie")
            }
        }
    }

    override fun newImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(AnimatedImageDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

}
