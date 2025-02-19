package com.example.eventapp.navigation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.core.network.models.Review
import com.example.eventapp.viewmodels.GetEventViewModel
import com.example.eventapp.viewmodels.GetEventViewModelFactory
import com.example.eventapps.ReviewViewDetail
import com.example.ws.repository.EventRepository

@Composable
fun EventDetailScreen(
    navController: NavController,
    eventId: Int,
    eventRepository: EventRepository // ✅ Dodali smo eventRepository kao argument
) {
    val factory = remember { GetEventViewModelFactory(eventRepository) }
    val viewModel: GetEventViewModel = viewModel(factory = factory) // ✅ Pravilno inicijalizujemo ViewModel

    val event by viewModel.eventDetails.observeAsState()

    LaunchedEffect(eventId) {
        viewModel.fetchEventDetails(eventId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        event?.let { e ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .padding(16.dp)
            ) {
                Header()
                Text(text = "Event Details", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Description: ${e.description}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Location: ${e.location}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Date: ${e.dateTime}", style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("reviewInputScreen/$eventId") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text(text = "Write a Review", color = Color.White)
                }
            }

            Box(modifier = Modifier.fillMaxHeight()) {
                ReviewViewDetail(eventId, viewModel)
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}


