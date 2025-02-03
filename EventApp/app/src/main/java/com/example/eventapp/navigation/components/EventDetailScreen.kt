package com.example.eventapp.navigation.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eventapp.models.Review
import com.example.eventapp.services.ReviewClient

@Composable
fun EventDetailScreen(navController: NavController, eventId: String) {
    // Pretvaranje eventId u Int
    val eventIdInt = eventId.toIntOrNull() ?: 0

    var eventName by remember { mutableStateOf("Music Festival") }
    var eventDescription by remember { mutableStateOf("Lorem ipsum dolor sit amet, consectetur adipiscing elit.") }
    var eventDate by remember { mutableStateOf("2023-06-01") }

    // State za recenzije
    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Dohvat recenzija za event
    LaunchedEffect(eventId) {
        try {
            val response = ReviewClient.reviewApi.getReviewsByEvent(eventIdInt)
            reviews = response
        } catch (e: Exception) {
            Log.e("EventDetail", "Error fetching reviews: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Detalji o događaju
        Text(text = eventName, style = MaterialTheme.typography.headlineLarge)
        Text(text = eventDate, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = eventDescription, style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Recenzije
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = "Reviews", style = MaterialTheme.typography.headlineSmall)

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(reviews) { review ->
                    //   ReviewItem(
                    //   text = review.text
                    // )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Gumb za pisanje recenzije
        Button(
            onClick = { navController.navigate("reviewInputScreen") },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(text = "Write a review", color = Color.White)
        }
    }
}

@Composable
fun ReviewItem(author: String, rating: Int, likes: Int, dislikes: Int, text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Autor i datum recenzije
            Text(text = author, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))

            // Zvjezdice
            StarRating(rating = rating)
            Spacer(modifier = Modifier.height(8.dp))

            // Tekst recenzije
            Text(text = text, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))

            // Lajkovi i dislajkovi
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(text = "$likes Likes", style = MaterialTheme.typography.bodySmall)
                Text(text = "$dislikes Dislikes", style = MaterialTheme.typography.bodySmall)
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

@Preview(showBackground = true)
@Composable
fun PreviewEventDetailScreen() {
    val navController = rememberNavController()
    EventDetailScreen(navController = navController, eventId = "1")
}
