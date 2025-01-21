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
import kotlinx.coroutines.delay

// Simulacija učitavanja chatova sa backend-a
suspend fun fetchChatsFromBackend(): List<Chat> {
    delay(2000) // Simulacija kašnjenja mrežnog poziva
    return listOf(
        Chat(1, "Family Chat"),
        Chat(2, "Work Group"),
        Chat(3, "Friends"),
        Chat(4, "John Doe")
    )
}

// Composable za prikaz pojedinog chata
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
        Text(chat.name, fontSize = 14.sp, color = Color.White)
    }
}

// Glavna funkcija za prikaz liste chatova
@Composable
fun ChatListScreen(navController: NavController) {
    var chats by remember { mutableStateOf<List<Chat>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Simulacija mrežnog poziva
    LaunchedEffect(Unit) {
        chats = fetchChatsFromBackend()
        isLoading = false
    }
    Column(
        modifier = Modifier
            .fillMaxSize() // Ako treba dodatni razmak
    ) {
        Header()
        Box(modifier = Modifier.fillMaxSize()) {
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
                        // Postavljanje clickable direktno na ChatCard
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
    }
}
