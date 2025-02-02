package com.example.eventapp.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OrganizerRetrofit {
    //nakon deployanja staviti azureportal
    private const val BASE_URL = "http://10.0.0.2:5205/"

    val api: OrganizerAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrganizerAPI::class.java)
    }
}
