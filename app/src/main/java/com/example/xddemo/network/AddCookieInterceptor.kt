package com.example.xddemo.network

import okhttp3.Interceptor
import okhttp3.Response

class AddCookieInterceptor(cookie: String = "") : Interceptor {
    @Volatile
    var cookie: String = cookie
        @Synchronized set
        @Synchronized get

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // 添加 Cookie 到请求头部
        val currentCookie = cookie
        val request = original.newBuilder()
            .addHeader("Cookie", currentCookie)
            .method(original.method, original.body)
            .build()

        return chain.proceed(request)
    }
}