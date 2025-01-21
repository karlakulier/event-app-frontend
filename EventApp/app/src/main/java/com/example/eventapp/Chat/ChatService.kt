package com.example.eventapp.Chat

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChatService : IChatService {
    private val api: IChatApi

    init {
        val okHttpClient = OkHttpClient.Builder()
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.xxx.xxx:5205/") // Zamijeni stvarnim URL-om
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
}
