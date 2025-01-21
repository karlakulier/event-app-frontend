package com.example.eventapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eventapp.models.Review

@Composable
fun ReviewDetailScreen(navController: NavController, review: Review) {
    var updatedLikes by remember { mutableStateOf(review.likes) }
    var updatedDislikes by remember { mutableStateOf(review.dislikes) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Back Button
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Name of Event and Date
        Text(text = "Name of event and Date", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        // Review Details
        Text(text = "${review.author} - ${review.date}", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        // Rating (Stars)
        Row {
            repeat(review.rating) {
                Icon(imageVector = Icons.Default.Star, contentDescription = "Star", tint = Color.Yellow)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Review Text
        Text(text = review.text, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Likes and Dislikes
        Row {
            IconButton(onClick = { updatedLikes++ }) {
                Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "Like", tint = Color.Green)
            }
            Text(text = "$updatedLikes Likes", fontSize = 14.sp)

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(onClick = { updatedDislikes++ }) {
                Icon(imageVector = Icons.Default.ThumbDown, contentDescription = "Dislike", tint = Color.Red)
            }
            Text(text = "$updatedDislikes Dislikes", fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReviewDetailScreen() {
    val navController = rememberNavController()
    val review = Review(
        id = 1,
        author = "John Doe",
        date = "22nd Jan 2025",
        rating = 5,
        text = "This was amazing!!! Lorem Ipsum is simply dummy text of the printing and typesetting industry...",
        likes = 243,
        dislikes = 12
    )

    ReviewDetailScreen(navController = navController, review = review)
}
