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

//zamejna za dohvat sa servera
suspend fun fetchMessagesForChat(chatId: Int): List<Message> {
    delay(1000) // Simulacija kašnjenja mrežnog poziva
    return listOf(
        Message("Marko",  "Hello!"),
        Message("Pero",  "Hi, how are you?"),
        Message("Ivan",  "I'm fine, thanks!"),
    )
}

@Composable
fun MessageListScreen(chatId: Int) {
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Dohvaćanje poruka za određeni chat
    LaunchedEffect(chatId) {
        messages = fetchMessagesForChat(chatId)
        isLoading = false
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Header()
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                Text(
                    text = "Loading messages...",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            } else if (messages.isEmpty()) {
                Text(
                    text = "No messages available.",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(messages) { message ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .background(Color.LightGray.copy(alpha = 0.2f))
                        ) {
                            Text(
                                text = message.userName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = message.content, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}