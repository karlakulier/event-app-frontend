package com.example.eventapp.Chat

class ChatController(private val chatService: IChatService) {
    suspend fun fetchChats(userId :Int): List<Chat> {
        return chatService.getChats(userId)
    }
}