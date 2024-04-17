package com.example.capstonedesign

import android.content.Context
import android.content.SharedPreferences
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.prefs.Preferences

object RetrofitClient {
    private const val BASE_URL = "http://15.165.22.20" // 실제 서버 주소로 변경해야 합니다.
    fun setOkHttpClient(context: Context) : OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }
    fun setRetroFitInstanceWithToken(context: Context) : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(setOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val retrofitInstance: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}