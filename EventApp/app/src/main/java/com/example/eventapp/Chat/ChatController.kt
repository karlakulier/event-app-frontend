package com.example.eventapp.Chat

class ChatController(private val chatService: IChatService) {
    suspend fun fetchChats(userId :Int): List<Chat> {
        return chatService.getChats(userId)
    }
    suspend fun fetchMessages(chatId :Int): List<Message> {
        return chatService.getMessages(chatId)
    }
}