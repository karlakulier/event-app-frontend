package com.example.eventapp.navigation.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ws.models.GetEventData
import com.example.eventapp.viewmodels.GetEventViewModel

@Composable
fun PastEvents(navController: NavController, organizerId: Int) {
    val viewModel: GetEventViewModel = viewModel()
    val events by viewModel.events.observeAsState(emptyList())

    // Fetch past events for the organizer
    LaunchedEffect(organizerId) {
        viewModel.fetchPastEventsByOrganizer(organizerId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Text(text = "Past Events", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6200EE))

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            if (events.isEmpty()) {
                Text(
                    text = "No past events available.",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(events) { event ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Navigate to the event details screen
                                    navController.navigate("eventDetails/${event.id}")
                                }
                        ) {
                            EventCard(event = event)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun EventCard(event: GetEventData) {
    // Display individual event card
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(text = event.description, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = "Location: ${event.location}", fontSize = 16.sp, color = Color.Gray)
        Text(text = "Date & Time: ${event.dateTime}", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
    }
}
