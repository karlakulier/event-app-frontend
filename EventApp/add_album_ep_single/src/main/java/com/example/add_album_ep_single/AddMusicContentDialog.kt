package com.example.add_album_ep_single

import com.example.core.login.Auth
import com.example.ws.models.AlbumData
import com.example.ws.models.EpData
import com.example.ws.models.SongData
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AddMusicContentDialog(
    isVisible: Boolean,
    contentType: MusicContentType,
    onDismiss: () -> Unit,
    onAddContent: (Any) -> Unit
) {
    if (isVisible) {
        var name by remember { mutableStateOf("") }
        var releaseDate by remember { mutableStateOf("") }
        val songs = remember { mutableStateListOf<SongData>() }
        var showDatePicker by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                    onClick = {
                        when (contentType) {
                            MusicContentType.ALBUM -> {
                                val newAlbum = AlbumData(
                                    id = 0,
                                    name = name,
                                    releaseDate = releaseDate,
                                    musicianId = Auth.loggedInUserData?.musician?.id ?: 0,
                                    existingSongs = songs,
                                    newSongs = songs
                                )
                                onAddContent(newAlbum)
                            }
                            MusicContentType.EP -> {
                                val newEP = EpData(
                                    id = 0,
                                    name = name,
                                    releaseDate = releaseDate,
                                    musicianId = Auth.loggedInUserData?.musician?.id ?: 0,
                                    existingSongs = songs,
                                    newSongs = songs
                                )
                                onAddContent(newEP)
                            }
                            MusicContentType.SONG -> {
                                val newSong = SongData(
                                    id = 0,
                                    name = name,
                                    linkYT = "",
                                    linkSpotify = "",
                                    musicianId = Auth.loggedInUserData?.musician?.id ?: 0,
                                    albumId = null,
                                    epId = null
                                )
                                onAddContent(newSong)
                            }
                        }
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
                    text = when (contentType) {
                        MusicContentType.ALBUM -> "ADD NEW ALBUM"
                        MusicContentType.EP -> "ADD NEW EP"
                        MusicContentType.SONG -> "ADD NEW SONG"
                    },
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
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (contentType != MusicContentType.SONG) {
                        OutlinedTextField(
                            value = releaseDate,
                            onValueChange = {},
                            label = { Text("Release Date") },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = "Select date",
                                    modifier = Modifier.clickable { showDatePicker = true }
                                )
                            },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (contentType != MusicContentType.SONG) {
                        songs.forEachIndexed { index, song ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = song.name,
                                    onValueChange = { updatedValue ->
                                        songs[index] = song.copy(name = updatedValue)
                                    },
                                    label = { Text("Song Name") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Button(
                            onClick = {
                                songs.add(SongData(id = 0, name = "", linkYT = "", linkSpotify = "", musicianId = Auth.loggedInUserData?.musician?.id ?: 0, albumId = null, epId = null))
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Song", tint = Color.White)
                        }
                    }
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

