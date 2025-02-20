package com.example.eventapp.navigation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import com.example.eventapps.ReviewViewDetail
import com.example.ws.repository.EventRepository
import com.example.ws.repository.OrganizerRepository

@Composable
fun EventDetailScreen(
    navController: NavController,
    eventId: Int,
    eventRepository: EventRepository
) {
    val factory = remember { GetEventViewModelFactory(eventRepository) }
    val viewModel: GetEventViewModel = viewModel(factory = factory)

    val event by viewModel.eventDetails.observeAsState()
    val loggedInUserRole = remember { Auth.loggedInUserData?.role }

    val organizerRepository = remember { OrganizerRepository() }
    var organizerName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(eventId) {
        viewModel.fetchEventDetails(eventId)
    }

    LaunchedEffect(event?.organizerId) {
        event?.organizerId?.let { organizerId ->
            val organizerProfile = organizerRepository.getOrganizerById(organizerId)
            organizerName = organizerProfile?.name ?: "Unknown Organizer"
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
        ) {
            Header(navController)

            event?.let { e ->
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

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    e.organizerId?.let { id ->
                                        navController.navigate("organizerProfileViewOnly/$id")
                                    }
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Organizer Profile",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = organizerName ?: "Loading...",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Divider(color = Color.LightGray, thickness = 1.dp)


                        Text(
                            text = "📍 Location: ${e.location}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )

                        Text(
                            text = "📅 Date: ${e.dateTime}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )

                        if (loggedInUserRole == "Visitor") {
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { navController.navigate("reviewInputScreen/$eventId") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                            ) {
                                Text(text = "Write a Review", color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    ReviewViewDetail(eventId, viewModel)
                }
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            if (loggedInUserRole == "Organizer") {
                OrganizerFooter(navController = navController)
            } else {
                ClassicFooter(navController = navController)
            }
        }
    }
}
