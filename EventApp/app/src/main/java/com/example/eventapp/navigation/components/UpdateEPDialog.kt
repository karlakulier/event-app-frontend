package com.example.eventapp.navigation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.ws.models.EpData
import com.example.ws.models.SongData
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun UpdateEPDialog(
    isVisible: Boolean,
    ep: EpData,
    onDismiss: () -> Unit,
    onUpdate: (EpData) -> Unit
) {
    if (isVisible) {
        val epName = remember { mutableStateOf(ep.name) }
        val releaseDate = remember { mutableStateOf(ep.releaseDate) }

        val existingSongs = remember { mutableStateListOf<SongData>().apply { addAll(ep.existingSongs) } }
        val newSongs = remember { mutableStateListOf<SongData>() }

        var showDatePicker by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(
                    onClick = {
                        val updatedEP = ep.copy(
                            name = epName.value,
                            releaseDate = releaseDate.value,
                            existingSongs = existingSongs,
                            newSongs = newSongs
                        )
                        onUpdate(updatedEP)
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
            title = { Text("EDIT EP", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        value = epName.value,
                        onValueChange = { epName.value = it },
                        label = { Text("EP Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = releaseDate.value,
                        onValueChange = {},
                        label = { Text("Release Date") },
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

                    // Prikaz postojećih pjesama
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

                    // Prikaz novih pjesama
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

                    Button(
                        onClick = {
                            newSongs.add(
                                SongData(
                                    id = 0, // Nova pjesma ima ID 0
                                    name = "",
                                    linkYT = "",
                                    linkSpotify = "",
                                    musicianId = ep.musicianId,
                                    albumId = null,
                                    epId = ep.id
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

