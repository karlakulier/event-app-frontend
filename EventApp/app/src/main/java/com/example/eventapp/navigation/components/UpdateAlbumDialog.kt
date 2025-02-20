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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.ws.models.AlbumData
import com.example.ws.models.SongData

@Composable
fun UpdateAlbumDialog(
    isVisible: Boolean,
    album: AlbumData,
    onDismiss: () -> Unit,
    onUpdate: (AlbumData) -> Unit
) {
    if (isVisible) {
        val albumName = remember { mutableStateOf(album.name) }
        val releaseDate = remember { mutableStateOf(album.releaseDate) }

        val existingSongs = remember { mutableStateListOf<SongData>().apply { addAll(album.existingSongs) } }
        val newSongs = remember { mutableStateListOf<SongData>() }

        var showDatePicker by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(
                    onClick = {
                        val updatedAlbum = album.copy(
                            name = albumName.value,
                            releaseDate = releaseDate.value,
                            existingSongs = existingSongs,
                            newSongs = newSongs
                        )
                        onUpdate(updatedAlbum)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Save Changes", color = Color.White)
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
            title = { Text("EDIT ALBUM", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        value = albumName.value,
                        onValueChange = { albumName.value = it },
                        label = { Text("Album name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    existingSongs.forEachIndexed { index, song ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Text("${index + 1}. Song", fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = song.name,
                                onValueChange = { updatedValue ->
                                    existingSongs[index] = song.copy(name = updatedValue)
                                },
                                label = { Text("Name") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = song.linkYT ?: "",
                                onValueChange = { updatedValue ->
                                    existingSongs[index] = song.copy(linkYT = updatedValue)
                                },
                                label = { Text("Youtube link") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = song.linkSpotify ?: "",
                                onValueChange = { updatedValue ->
                                    existingSongs[index] = song.copy(linkSpotify = updatedValue)
                                },
                                label = { Text("Spotify link") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    newSongs.forEachIndexed { index, song ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .border(1.dp, Color.Blue, RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Text("${existingSongs.size + index + 1}. New Song", fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = song.name,
                                onValueChange = { updatedValue ->
                                    newSongs[index] = song.copy(name = updatedValue)
                                },
                                label = { Text("Name") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = song.linkYT ?: "",
                                onValueChange = { updatedValue ->
                                    newSongs[index] = song.copy(linkYT = updatedValue)
                                },
                                label = { Text("Youtube link") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = song.linkSpotify ?: "",
                                onValueChange = { updatedValue ->
                                    newSongs[index] = song.copy(linkSpotify = updatedValue)
                                },
                                label = { Text("Spotify link") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            newSongs.add(
                                SongData(
                                    id = 0,
                                    name = "",
                                    linkYT = "",
                                    linkSpotify = "",
                                    musicianId = album.musicianId,
                                    albumId = album.id,
                                    epId = null
                                )
                            )
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Song", tint = Color.White)
                    }
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )

        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { displayDate, apiDate ->
                    releaseDate.value = apiDate
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}
