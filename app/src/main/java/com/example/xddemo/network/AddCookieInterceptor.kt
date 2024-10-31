package com.example.xddemo.network

import okhttp3.Interceptor
import okhttp3.Response

class AddCookieInterceptor(private val cookie: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // 添加 Cookie 到请求头部
        val request = original.newBuilder()
            .addHeader("Cookie", cookie)
            .method(original.method, original.body)
            .build()

        return chain.proceed(request)
    }
}