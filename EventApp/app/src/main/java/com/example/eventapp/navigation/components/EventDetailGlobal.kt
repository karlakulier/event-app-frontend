package com.example.eventapp.navigation.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.eventapp.viewmodels.GetEventViewModel
import com.example.eventapps.ReviewViewDetail
import com.example.ws.models.EventDetails
import com.example.ws.models.GetEventData
import com.example.ws.repository.EventRepository
import com.example.ws.services.ChatService
import kotlinx.coroutines.launch

@Composable
fun EventDetailsGlobal(eventId: Int, navController: NavController) {
    var isLoading by remember { mutableStateOf(true) }
    val eventService = EventRepository()
    var eventData by remember { mutableStateOf<EventDetails?>(null) }
    var cancelMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(eventId) {
        eventData = eventService.getEventDetailsGlobal(eventId)
        isLoading = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
        ) {
            Header(navController)

            if (isLoading) {
                Text(
                    text = "Lodading...",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (eventData != null) {
                eventData?.let { e ->
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = e.description,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .wrapContentHeight()
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "🎵 Glazbeni žanr: ${e.musicGenre}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "📍 Lokacija: ${e.location}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "📅 Datum i vrijeme: ${e.dateTime}", style = MaterialTheme.typography.bodyLarge)
                            Divider(color = Color.LightGray, thickness = 1.dp)

                            if (e.musicians.isNotEmpty()) {
                                Text(
                                    text = "🎤 Glazbenici:",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                ) {
                                    items(e.musicians) { musician ->
                                        Text(text = musician, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
                                    }
                                }
                            } else {
                                Text(text = "Nema prijavljenih glazbenika.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (loggedInUserData?.role == "Organizer" && eventData?.organizerId == loggedInUserData?.organizer?.id!!) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            Button(
                                onClick = { navController.navigate("applicationsForEvent/${eventId}") },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text(text = "Applications", color = Color.White)
                            }

                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        val success = eventService.cancelEvent(eventId)
                                        cancelMessage = if (success) {
                                            "Otkazali ste događaj"
                                        } else {
                                            "Došlo je do pogreške pri otkazivanju događaja"
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                            ) {
                                Text(text = "Cancel", color = Color.White)
                            }
                        }
                    }

                    cancelMessage?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                Text(
                    text = "Event not found.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            if (loggedInUserData?.role == "Musician") {
                ClassicFooter(navController)
            } else if (loggedInUserData?.role == "Organizer") {
                OrganizerFooter(navController)
            }
        }
    }
}





