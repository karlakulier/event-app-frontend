package com.example.eventapp.navigation.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.network.models.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.core.chat.IChatService
import com.example.ws.services.ChatService


@Composable
fun MessageListScreen(chatId: Int) {
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val chatController:IChatService = ChatService()
    val context = LocalContext.current

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        while (true){
            isLoading = true
            messages = chatController.getMessages(chatId)
            isLoading = false
            delay(10000)
        }
    }

    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            listState.scrollToItem(messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Header()

        Box(modifier = Modifier.weight(1f).fillMaxSize()) {
            if (isLoading && messages.isEmpty() ) {
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
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    items(messages) { message ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .background(Color.LightGray.copy(alpha = 0.2f))
                        ) {
                            Text(
                                text = message.nameOfSender,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = message.text, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var newMessageText by remember { mutableStateOf("") }

            TextField(
                value = newMessageText,
                onValueChange = { newMessageText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                placeholder = { Text(text = "Enter your message") },
                maxLines = 1,
                singleLine = true
            )

            IconButton(
                onClick = {
                    if (newMessageText.isNotBlank()) {
                        val newMessage = Message("Vedran", newMessageText, chatId)
                        newMessageText = ""
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                chatController.sendMessage(newMessage)
                                val updatedMessages = chatController.getMessages(chatId)

                                withContext(Dispatchers.Main) {
                                    messages = updatedMessages
                                    Toast.makeText(context, "Message successfully sent!", Toast.LENGTH_SHORT).show()
                                }

                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.Blue
                )
            }
        }
    }
}

