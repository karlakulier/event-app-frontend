package com.example.eventapp.Chat


interface IChatService {
    suspend fun getChats(userID : Int): List<Chat>
    suspend fun getMessages( chatId: Int): List<Message>
}