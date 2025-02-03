package com.example.eventapp.Chat

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ChatService : IChatService {
    private val api: IChatApi

    init {
        val okHttpClient = OkHttpClient.Builder()
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://aireventapp.azurewebsites.net/") // Zamijeni stvarnim URL-om
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(IChatApi::class.java)
    }

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
