package com.example.eventapp.navigation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
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
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AddNewAlbumDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onAddAlbum: (String, String, List<Map<String, String>>) -> Unit
) {
    if (isVisible) {
        val albumName = remember { mutableStateOf("") }
        val releaseDate = remember { mutableStateOf("") }
        val songs = remember { mutableStateListOf<Map<String, String>>() }
        var showDatePicker by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(
                    onClick = {
                        onAddAlbum(albumName.value, releaseDate.value, songs)
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
                    text = "ADD NEW ALBUM",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                ) {
                    // Album name input
                    OutlinedTextField(
                        value = albumName.value,
                        onValueChange = { albumName.value = it },
                        label = { Text("Album name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Release date input with DatePicker
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Songs Section
                    songs.forEachIndexed { index, song ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "${index + 1}. Song",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            OutlinedTextField(
                                value = song["name"] ?: "",
                                onValueChange = { updatedValue ->
                                    songs[index] = song.toMutableMap().apply { put("name", updatedValue) }
                                },
                                label = { Text("Name") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = song["youtubeLink"] ?: "",
                                onValueChange = { updatedValue ->
                                    songs[index] = song.toMutableMap().apply { put("youtubeLink", updatedValue) }
                                },
                                label = { Text("Youtube link") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = song["spotifyLink"] ?: "",
                                onValueChange = { updatedValue ->
                                    songs[index] = song.toMutableMap().apply { put("spotifyLink", updatedValue) }
                                },
                                label = { Text("Spotify link") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Add Song Button
                    Button(
                        onClick = {
                            songs.add(
                                mapOf(
                                    "name" to "",
                                    "youtubeLink" to "",
                                    "spotifyLink" to ""
                                )
                            )
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Song", tint = Color.White)
                    }
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )

        // DatePicker Modal
        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { millis ->
                    releaseDate.value = millis?.let { convertMillisToDate(it) } ?: ""
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}


