package com.example.xddemo.network

import com.example.xddemo.data.model.Reply
import com.example.xddemo.data.model.ThreadPage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface XDaoApiService {

    @GET("Api/thread")
    suspend fun getThreadPage(
        @Query("id") id: Int,
        @Query("page") page: Int
    ): ThreadPage

}