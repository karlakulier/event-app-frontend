package com.example.eventapp.Chat

import retrofit2.http.GET
import retrofit2.http.Query

interface IChatApi {
    @GET("api/Chat/ByUser")
    suspend fun getCars(@Query("userId") userId: Int): List<Chat>
}