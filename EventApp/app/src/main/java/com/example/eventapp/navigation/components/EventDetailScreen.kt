package com.example.eventapp.navigation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
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
import com.example.eventapp.models.Review
import com.example.eventapp.viewmodels.GetEventViewModel
import com.example.ws.models.GetEventData

@Composable
fun EventDetailScreen(navController: NavController, eventId: Int) {
    val viewModel: GetEventViewModel = viewModel()
    val event by viewModel.eventDetails.observeAsState()

    LaunchedEffect(eventId) {
        viewModel.fetchEventDetails(eventId)
    }

    event?.let { e ->
        Column(modifier = Modifier.padding(16.dp)) {
            // Display event details
            Text(text = "Event Details", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Description: ${e.description}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Location: ${e.location}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Date: ${e.dateTime}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(16.dp))

            // Recenzije
            Text(text = "Reviews", style = MaterialTheme.typography.headlineSmall)

            // LazyColumn with reviews
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Fill remaining space
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Assuming we have reviews from backend (mocked here)
               // val reviews = listOf(
                    //Review(id = 1, author = "Artist", rating = 5, text = "Great experience!", likes = 100),
                  //  Review(id = 2, author = "Jane Irwing", rating = 4, text = "Good event, but could be better.")
               // )

               // items(reviews) { review ->
               //     ReviewItem(review)
              //  }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Spacer before button

            // Write review button
            Button(
                onClick = {
                    // Navigate to review input screen
                    navController.navigate("reviewInputScreen")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text(text = "Write a review", color = Color.White)
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Card(modifier = Modifier.fillMaxWidth().padding(4.dp), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = review.author, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Rating: ${review.rating} ★", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = review.text, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${review.likes} Likes", style = MaterialTheme.typography.bodySmall)
                Text(text = "${review.dislikes} Dislikes", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun StarRating(rating: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "Star $i",
                tint = if (rating >= i) Color.Yellow else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
