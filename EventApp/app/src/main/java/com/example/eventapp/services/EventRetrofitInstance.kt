package com.example.eventapp.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object EventRetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:5205/"

    val apievent: EventService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventService::class.java)
    }
}