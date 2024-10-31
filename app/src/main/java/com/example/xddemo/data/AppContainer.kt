package com.example.xddemo.data

import android.content.Context
import com.example.xddemo.data.repository.ThreadRepository
import com.example.xddemo.network.AddCookieInterceptor
import com.example.xddemo.network.XDaoApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val threadRepository: ThreadRepository
}

class DefaultAppContainer(
    private val context: Context,
    private val cookie: String
) : AppContainer {

    private val baseUrl = "https://api.nmb.best/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(AddCookieInterceptor(cookie))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitService: XDaoApiService by lazy {
        retrofit.create(XDaoApiService::class.java)
    }

    override val threadRepository: ThreadRepository by lazy {
        ThreadRepository(retrofitService, XDaoDatabase.getDatabase(context).threadDao())
    }

}