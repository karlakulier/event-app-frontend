package com.example.eventapp.navigation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AddNewSongDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onAddSong: (String, String, String, String) -> Unit
) {
    if (isVisible) {
        val songName = remember { mutableStateOf("") }
        val releaseDate = remember { mutableStateOf("") }
        val youtubeLink = remember { mutableStateOf("") }
        val spotifyLink = remember { mutableStateOf("") }
        var showDatePicker by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(
                    onClick = {
                        onAddSong(songName.value, releaseDate.value, youtubeLink.value, spotifyLink.value)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Add", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel", color = Color.White)
                }
            },
            title = {
                Text(
                    text = "ADD NEW SONG",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    // Song name input
                    OutlinedTextField(
                        value = songName.value,
                        onValueChange = { songName.value = it },
                        label = { Text("Song name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Release date input
                    OutlinedTextField(
                        value = releaseDate.value,
                        onValueChange = {},
                        label = { Text("Release date") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = "Select date",
                                modifier = Modifier.clickable { showDatePicker = true }
                            )
                        },
                        readOnly = true, // Sprečava direktno uređivanje
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // YouTube link input
                    OutlinedTextField(
                        value = youtubeLink.value,
                        onValueChange = { youtubeLink.value = it },
                        label = { Text("Youtube link") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Spotify link input
                    OutlinedTextField(
                        value = spotifyLink.value,
                        onValueChange = { spotifyLink.value = it },
                        label = { Text("Spotify link") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { displayDate, apiDate ->  // Očekujemo dva parametra
                    releaseDate.value = displayDate  // Prikaz korisniku
                    // Ovdje možeš spremiti apiDate ako ga trebaš slati u API
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}
