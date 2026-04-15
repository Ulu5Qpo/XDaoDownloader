package com.example.xddemo.network

import okhttp3.Call
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test

class AddCookieInterceptorInstrumentedTest {

    @Test
    fun intercept_addsLatestCookieHeader() {
        val interceptor = AddCookieInterceptor("userhash=first")
        interceptor.cookie = "userhash=updated"
        val chain = FakeChain(
            Request.Builder()
                .url("https://example.com/Api/thread?id=1&page=1")
                .build()
        )

        interceptor.intercept(chain)

        assertEquals("userhash=updated", chain.proceededRequest.header("Cookie"))
    }

    private class FakeChain(
        private val originalRequest: Request
    ) : Interceptor.Chain {
        lateinit var proceededRequest: Request

        override fun request(): Request = originalRequest

        override fun proceed(request: Request): Response {
            proceededRequest = request
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body("ok".toResponseBody())
                .build()
        }

        override fun connection(): Connection? = null

        override fun call(): Call {
            throw UnsupportedOperationException("Not needed in test")
        }

        override fun connectTimeoutMillis(): Int = 0

        override fun withConnectTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit): Interceptor.Chain = this

        override fun readTimeoutMillis(): Int = 0

        override fun withReadTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit): Interceptor.Chain = this

        override fun writeTimeoutMillis(): Int = 0

        override fun withWriteTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit): Interceptor.Chain = this
    }
}
