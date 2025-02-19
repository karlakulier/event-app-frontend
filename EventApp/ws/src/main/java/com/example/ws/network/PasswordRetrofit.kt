package com.example.ws.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PasswordRetrofit {
    private const val BASE_URL = "https://aireventappapinew.azurewebsites.net/"

    val api: PasswordAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PasswordAPI::class.java)
    }
}