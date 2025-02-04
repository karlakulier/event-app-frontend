package com.example.ws.network

import com.example.ws.services.EventService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object EventRetrofitInstance {

    private const val BASE_URL = "https://aireventapp.azurewebsites.net/"

    val apievent: EventService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventService::class.java)
    }
}