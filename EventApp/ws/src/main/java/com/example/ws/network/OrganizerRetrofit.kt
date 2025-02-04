package com.example.ws.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OrganizerRetrofit {
    private const val BASE_URL = "https://aireventapp.azurewebsites.net/"

    val api: OrganizerAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrganizerAPI::class.java)
    }
}
