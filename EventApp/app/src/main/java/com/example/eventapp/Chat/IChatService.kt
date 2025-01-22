package com.example.eventapp.Chat

import retrofit2.Response
import retrofit2.http.Body


interface IChatService {
    suspend fun getChats(userID : Int): List<Chat>
    suspend fun getMessages( chatId: Int): List<Message>
    suspend fun sendMessage(message: Message): Response<Void>
}