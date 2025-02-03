package com.example.eventapp.services

import IReviewApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ReviewClient {
    private const val BASE_URL = "https://aireventapp.azurewebsites.net/"

    val reviewApi: IReviewApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IReviewApi::class.java)
    }
}
