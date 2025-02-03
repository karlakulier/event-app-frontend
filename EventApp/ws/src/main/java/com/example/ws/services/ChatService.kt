package com.example.ws.services
import com.example.core.chat.IChatService
import com.example.core.network.models.Chat
import com.example.core.network.models.Message
import com.example.eventapp.Chat.IChatApi

import com.example.ws.network.RetrofitClient.retrofit

import retrofit2.Response


class ChatService : IChatService {

    val api: IChatApi = retrofit.create(IChatApi::class.java)


    override suspend fun getChats(userId : Int): List<Chat> {
        return api.getChats(userId)
    }

    override suspend fun getMessages(chatId: Int): List<Message> {
        return api.getMessages(chatId)
    }

    override suspend fun sendMessage(message: Message): Response<Void> {
        return try {
            val response = api.sendMessage(message)
            if (!response.isSuccessful) {
                throw Exception("Neuspješan zahtev: HTTP ${response.code()} - ${response.message()}")
            }
            response
        }catch(e: Exception){
            throw Exception("Greška prilikom slanja poruke: ${e.message}")
        }
    }

}
