package com.example.eventapp.navigation.components

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

@Composable
fun EventDetailScreen(navController: NavController, eventId: String) {
    // Pretpostavljamo da ovdje dohvaćate detalje o događaju prema eventId-u
    var eventName by remember { mutableStateOf("Music Festival") }
    var eventDescription by remember { mutableStateOf("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce vel est at elit cursus luctus.") }
    var eventDate by remember { mutableStateOf("2023-06-01") }

    // Placeholder za recenzije, u stvarnom životu bi ovo došlo iz baze podataka ili API-ja
    val reviews = listOf(
        Review(id = 1, author = "Artist", date = "2023-06-01", rating = 5, text = "Thank you everyone! Lorem Ipsum is simply dummy text...", likes = 369, dislikes = 32),
        Review(id = 2, author = "Jane Irwing", date = "2023-06-02", rating = 4, text = "Great experience!!! Lorem Ipsum is simply dummy text...", likes = 163, dislikes = 31)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Naziv događaja
        Text(text = eventName, style = MaterialTheme.typography.headlineLarge)

        // Datum događaja
        Text(text = eventDate, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)

        // Opis događaja
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = eventDescription, style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Recenzije
        Text(text = "Reviews", style = MaterialTheme.typography.headlineSmall)

        // LazyColumn s ograničenom visinom
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),  // Ovdje dodajemo .weight(1f) kako bi LazyColumn zauzeo raspoloživi prostor
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(reviews) { review ->
                ReviewItem(
                    author = review.author,
                    rating = review.rating,
                    likes = review.likes,
                    dislikes = review.dislikes,
                    text = review.text
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Razmak prije gumba

        // Gumb za dodavanje recenzije
        Button(
            onClick = {
                navController.navigate("reviewInputScreen") // Navigacija na ekran za unos recenzije
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Povećana visina gumba
                .padding(bottom = 16.dp), // Dodajemo padding da ne bude preblizu donjoj granici
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(text = "Write a review", color = Color.White)
        }
    }
}

@Composable
fun ReviewItem(
    author: String,
    rating: Int,
    likes: Int,
    dislikes: Int,
    text: String
) {
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
    // Mock NavController for Preview
    val navController = rememberNavController()

    EventDetailScreen(navController = navController, eventId = "1")
}
