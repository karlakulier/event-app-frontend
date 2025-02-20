package com.example.eventapp.navigation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SongInput(songNumber: Int, song: Song, onSongChange: (Song) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(text = "$songNumber. Song", fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = song.name,
            onValueChange = { onSongChange(song.copy(name = it)) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = song.youtubeLink,
            onValueChange = { onSongChange(song.copy(youtubeLink = it)) },
            label = { Text("Youtube link") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = song.spotifyLink,
            onValueChange = { onSongChange(song.copy(spotifyLink = it)) },
            label = { Text("Spotify link") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}


data class Song(
    var name: String,
    var youtubeLink: String,
    var spotifyLink: String
)