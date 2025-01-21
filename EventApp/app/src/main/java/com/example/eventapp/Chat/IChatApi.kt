package com.example.eventapp.Chat

import retrofit2.http.GET
import retrofit2.http.Query

interface IChatApi {
    @GET("api/Chat/ByUser")
    suspend fun getChats(@Query("userId") userId: Int): List<Chat>

    @GET("api/Messages/ByChat")
    suspend fun getMessages(@Query("chatId") chatId: Int): List<Message>
}