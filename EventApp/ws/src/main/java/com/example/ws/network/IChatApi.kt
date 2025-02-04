package com.example.eventapp.Chat

import com.example.core.network.models.Chat
import com.example.core.network.models.Message
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IChatApi {
    @GET("api/Chat/ByUser")
    suspend fun getChats(@Query("userId") userId: Int): List<Chat>

    @GET("api/Message/ByChatId")
    suspend fun getMessages(@Query("chatId") chatId: Int): List<Message>

    @POST("api/Message/SendMessage")
    suspend fun sendMessage(@Body message: Message): Response<Void>
}