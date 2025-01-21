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
import com.example.eventapp.navigation.components.ClassicFooter
import com.example.eventapp.navigation.components.Header
import com.example.eventapp.navigation.components.OrganizerFooter
import kotlinx.coroutines.launch

@Composable
fun ReviewInputScreen(navController: NavController) {
    var organizationRating by remember { mutableStateOf(0f) }
    var venueRating by remember { mutableStateOf(0f) }
    var reviewText by remember { mutableStateOf("") }

    // Using coroutine scope to show Toast
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Header()

        // Organization Rating
        Text(text = "Organization", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        RatingBar(rating = organizationRating, onRatingChanged = { organizationRating = it })

        Spacer(modifier = Modifier.height(16.dp))

        // Venue Rating
        Text(text = "Venue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        RatingBar(rating = venueRating, onRatingChanged = { venueRating = it })

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
                    // Show Toast using coroutine scope
                    coroutineScope.launch {
                        Toast.makeText(navController.context, "Review submitted!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    coroutineScope.launch {
                        Toast.makeText(navController.context, "Please write a review!", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Submit Review")
        }

        ClassicFooter(navController)
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
    // Mock NavController for Preview
    val navController = rememberNavController()

    ReviewInputScreen(navController = navController)
}
