package com.example.capstonedesign

import android.app.Activity
import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.util.prefs.Preferences

class AuthInterceptor(context : Context) : Interceptor {
    private var sharedPreferences = context.applicationContext.getSharedPreferences("APP", Context.MODE_PRIVATE)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = sharedPreferences.getString("token", null)
        if (token.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}