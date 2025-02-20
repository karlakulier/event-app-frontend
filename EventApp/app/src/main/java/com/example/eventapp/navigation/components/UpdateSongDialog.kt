package com.example.eventapp.navigation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.ws.models.SongData
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun UpdateSongDialog(
    isVisible: Boolean,
    song: SongData,
    onDismiss: () -> Unit,
    onUpdate: (SongData) -> Unit
) {
    if (isVisible) {
        val songName = remember { mutableStateOf(song.name) }
        val youtubeLink = remember { mutableStateOf(song.linkYT) }
        val spotifyLink = remember { mutableStateOf(song.linkSpotify) }

        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(
                    onClick = {
                        val updatedSong = song.copy(
                            name = songName.value,
                            linkYT = youtubeLink.value,
                            linkSpotify = spotifyLink.value
                        )
                        onUpdate(updatedSong)
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
            title = { Text("EDIT SONG", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            text = {
                Column {
                    OutlinedTextField(
                        value = songName.value,
                        onValueChange = { songName.value = it },
                        label = { Text("Song Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = youtubeLink.value.orEmpty(),
                        onValueChange = { youtubeLink.value = it },
                        label = { Text("YouTube Link") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = spotifyLink.value.orEmpty(),
                        onValueChange = { spotifyLink.value = it },
                        label = { Text("Spotify Link") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

