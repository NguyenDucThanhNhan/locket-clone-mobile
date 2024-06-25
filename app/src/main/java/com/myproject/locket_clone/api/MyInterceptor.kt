package com.myproject.locket_clone.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class MyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
            .newBuilder()
            .addHeader("api-key", "ABC-XYZ-WWW")
            .build()
        return chain.proceed(request)
    }
}
