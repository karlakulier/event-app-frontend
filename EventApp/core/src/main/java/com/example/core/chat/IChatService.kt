package com.example.core.chat
import com.example.core.network.models.Chat
import com.example.core.network.models.Message
import retrofit2.Response


interface IChatService {
    suspend fun getChats(userID : Int,role : String): List<Chat>
    suspend fun getMessages( chatId: Int): List<Message>
    suspend fun sendMessage(message: Message): Response<Void>
}