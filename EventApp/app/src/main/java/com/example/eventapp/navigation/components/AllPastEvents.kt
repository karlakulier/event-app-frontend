package com.example.eventapp.navigation.components

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
import com.example.core.login.Auth
import com.example.eventapp.viewmodels.GetEventViewModel
import com.example.eventapp.viewmodels.GetEventViewModelFactory
import com.example.ws.models.GetEventData
import com.example.ws.repository.EventRepository

@Composable
fun AllPastEventsScreen(
    navController: NavController,
    viewModel: GetEventViewModel = viewModel(factory = GetEventViewModelFactory(EventRepository()))
) {
    val pastEvents by viewModel.pastEvents.observeAsState(emptyList())
    val isOrganizer = Auth.loggedInUserData?.role == "Organizer"

    LaunchedEffect(Unit) {
        viewModel.fetchAllPastEvents()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
        ) {
            Header()
            Text("All Past Events", fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            if (pastEvents.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No past events available", fontSize = 18.sp, color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(pastEvents) { event ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("eventDetailsScreen/${event.id}")
                                }
                        ) {
                            EventCardPast(
                                event = event,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            if (isOrganizer) {
                OrganizerFooter(navController = navController)
            } else {
                ClassicFooter(navController = navController)
            }
        }
    }
}
