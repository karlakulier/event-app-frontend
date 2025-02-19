package com.example.eventapp.navigation.components

import com.example.core.login.Auth.loggedInUserData
import com.example.ws.models.GetEventData
import com.example.ws.repository.EventRepository
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ActiveEventListScreen(navController: NavController) {
    var events by remember { mutableStateOf<List<GetEventData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val eventService = EventRepository()

    val organizerId = loggedInUserData?.organizer?.id
    val userRole = loggedInUserData?.role

    LaunchedEffect(organizerId) {
        organizerId?.let {
            events = eventService.getActiveEventsByOrganizer(it)
        }
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Header sada ide skroz do vrha ekrana i zauzima punu širinu
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Header(navController) // Header sada ide skroz do vrha i preko cijele širine ekrana
        }

        // Naslov "Aktivni eventi" s paddingom samo horizontalno
        Text(
            text = "Aktivni eventi",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        Box(modifier = Modifier.weight(1f)) { // Lista evenata zauzima preostali prostor
            when {
                isLoading -> {
                    Text(
                        text = "Učitavanje...",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                events.isEmpty() -> {
                    Text(
                        text = "Nema aktivnih evenata.",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                        items(events) { event ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { navController.navigate("eventDetails/${event.id}") }
                                    .padding(vertical = 8.dp)
                            ) {
                                EventCard(event = event, navController)
                            }
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) { // Footer na dnu
            when (userRole) {
                "Musician" -> ClassicFooter(navController)
                "Organizer" -> OrganizerFooter(navController)
            }
        }
    }
}