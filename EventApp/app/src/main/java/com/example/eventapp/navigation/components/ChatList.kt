package com.example.eventapp.navigation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.core.chat.IChatService
import com.example.core.login.Auth.loggedInUserData
import com.example.core.network.models.Chat
import com.example.ws.services.ChatService

@Composable
fun ChatCard(chat: Chat) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(16.dp)
    ) {
        Text(chat.id.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(chat.name, fontSize = 14.sp)
    }
}


@Composable
fun ChatListScreen(navController: NavController) {
    var chats by remember { mutableStateOf<List<Chat>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val chatController: IChatService = ChatService()

    LaunchedEffect(Unit) {
        if(loggedInUserData?.role == "Musician"){
            chats = chatController.getChats(loggedInUserData?.musician?.id!!,"Musician")
            isLoading = false
        }else{
            chats = chatController.getChats(loggedInUserData?.organizer?.id!!,"Organizer")
            isLoading = false
        }

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Header(navController)
        Text(
            text = "All Chats",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Box(modifier = Modifier.weight(1f).fillMaxSize()) {
            if (isLoading) {
                Text(
                    text = "Loading...",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            } else if (chats.isEmpty()) {
                Text(
                    text = "No chats available.",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(chats) { chat ->

                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("messages/${chat.id}") // Pravilna destinacija
                            }) {
                            ChatCard(chat = chat)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        if(loggedInUserData?.role=="Musician"){
            ClassicFooter(navController)
        }else if(loggedInUserData?.role=="Organizer"){
            OrganizerFooter(navController)
        }
    }
}
