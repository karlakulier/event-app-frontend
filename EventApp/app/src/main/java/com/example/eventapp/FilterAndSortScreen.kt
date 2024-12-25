package com.example.eventapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun FilterAndSortPopup(onDismissRequest: () -> Unit, onApplyFilters: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "FILTER AND SORT",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )

                // Genre filter section
                Text(text = "Genre of music", fontSize = 16.sp, color = Color.Black)
                val genres = listOf("Blues", "Jazz", "Pop", "Rock", "Opera", "Electronic", "Country", "Punk", "Hip Hop", "Metal")
                val selectedGenres = remember { mutableStateListOf<String>() }

                Column {
                    genres.chunked(2).forEach { chunk ->
                        Row {
                            chunk.forEach { genre ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Checkbox(
                                        checked = selectedGenres.contains(genre),
                                        onCheckedChange = { checked ->
                                            if (checked) selectedGenres.add(genre)
                                            else selectedGenres.remove(genre)
                                        }
                                    )
                                    Text(text = genre, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }

                // Rating filter section
                Text(text = "Rating", fontSize = 16.sp, color = Color.Black)
                var minRating by remember { mutableStateOf(0) }
                var maxRating by remember { mutableStateOf(5) }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Min: $minRating")
                    Slider(
                        value = minRating.toFloat(),
                        onValueChange = { minRating = it.toInt() },
                        valueRange = 0f..5f,
                        steps = 4,
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                    )
                    Text(text = "Max: $maxRating")
                    Slider(
                        value = maxRating.toFloat(),
                        onValueChange = { maxRating = it.toInt() },
                        valueRange = 0f..5f,
                        steps = 4,
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                    )
                }

                // Application date filter section
                Text(text = "Application date", fontSize = 16.sp, color = Color.Black)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "From: 2023-01-01") // Placeholder
                    Text(text = "To: 2023-12-31")   // Placeholder
                }

                // Apply filters button
                Button(
                    onClick = onApplyFilters,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Apply filters")
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { showDialog = true }) {
            Text(text = "Open Filters")
        }
    }

    if (showDialog) {
        FilterAndSortPopup(
            onDismissRequest = { showDialog = false },
            onApplyFilters = { /* Handle apply filters */ showDialog = false }
        )
    }
}