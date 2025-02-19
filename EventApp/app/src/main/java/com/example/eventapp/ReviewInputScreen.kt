import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.core.network.models.Review
import kotlinx.coroutines.launch
import com.example.ws.network.RetrofitClient
import com.example.ws.services.ReviewService


@Composable
fun ReviewInputScreen(navController: NavController, reviewService: ReviewService, eventId: Int) {
    var organizationRating by remember { mutableStateOf(0f) }
    var venueRating by remember { mutableStateOf(0f) }
    var reviewText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }


    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Header
        Text("Write your Review", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))


        Text(text = "Organization", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        RatingBar(rating = organizationRating, onRatingChanged = { newRating -> organizationRating = newRating })

        Spacer(modifier = Modifier.height(16.dp))


        Text(text = "Venue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        RatingBar(rating = venueRating, onRatingChanged = { newRating -> venueRating = newRating })

        Spacer(modifier = Modifier.height(16.dp))

        // Review Text
        Text(text = "Write your review", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        BasicTextField(
            value = reviewText,
            onValueChange = { reviewText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.Gray.copy(alpha = 0.1f))
                .padding(8.dp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                if (reviewText.isNotEmpty()) {
                    isLoading = true
                    val review = Review(
                        id = 0, // ID će biti automatski generiran na backendu
                        text = reviewText,
                        musicianGrade = organizationRating.toInt(),
                        organizerGrade = venueRating.toInt(),
                        eventId = eventId,
                        visitorId = 1, //  stvarni ID posjetitelja
                        musicianId = 1, //  stvarni ID muzičara
                        organizerId = 1 //  stvarni ID organizatora
                    )

                    coroutineScope.launch {
                        try {
                            reviewService.createReview(review) // API za slanje recenzije
                            Toast.makeText(navController.context, "Review submitted successfully!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } catch (e: Exception) {
                            Toast.makeText(navController.context, "Failed to submit review: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    coroutineScope.launch {
                        Toast.makeText(navController.context, "Please write a review!", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(text = if (isLoading) "Submitting..." else "Submit Review", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RatingBar(rating: Float, onRatingChanged: (Float) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            IconButton(
                onClick = { onRatingChanged(i.toFloat()) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star $i",
                    tint = if (rating >= i) Color.Yellow else Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReviewInputScreen() {
    val navController = rememberNavController()
    val reviewService = ReviewService(reviewApi = RetrofitClient.reviewApi)
    ReviewInputScreen(navController = navController, reviewService = reviewService, eventId = 1)
}