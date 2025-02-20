package com.example.eventapp.navigation.components

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.core.login.Auth
import com.example.eventapp.R
import com.example.eventapp.viewmodels.ArtistProfileViewModel
import com.example.ws.models.SongData

@Composable
fun AlbumDetailsScreen(
    navController: NavController,
    albumId: Int,
    viewModel: ArtistProfileViewModel
) {
    var isEditAlbumDialogVisible by remember { mutableStateOf(false) }
    var isEditSongDialogVisible by remember { mutableStateOf(false) }
    var selectedSongForEdit by remember { mutableStateOf<SongData?>(null) }
    var isEditingSongs by remember { mutableStateOf(false) }

    val albums by viewModel.albums.collectAsState()
    val album = albums.find { it.id == albumId }
    var albumSongs by remember { mutableStateOf(album?.existingSongs ?: emptyList()) }

    LaunchedEffect(album?.existingSongs) {
        albumSongs = album?.existingSongs ?: emptyList()
    }

    if (album == null) {
        Text("Album not found")
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = album.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Text(
                    text = "Album · ${album.releaseDate}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            Row {
                val isCurrentMusician = Auth.loggedInUserData?.musician?.id == album.musicianId
                if (isCurrentMusician && !isEditingSongs) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_musician_profile),
                        contentDescription = "Edit Album",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { isEditAlbumDialogVisible = true }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.edit_musician_profile),
                        contentDescription = "Enable Song Editing",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { isEditingSongs = true }
                    )
                } else if (isCurrentMusician && isEditingSongs) {
                    Icon(
                        painter = painterResource(R.drawable.approve_edit),
                        contentDescription = "Save Changes",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { isEditingSongs = false }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(R.drawable.abandon_edit),
                        contentDescription = "Cancel Editing",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { isEditingSongs = false }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Divider(
            color = Color.Gray,
            thickness = 2.dp,
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn {
            items(albumSongs) { song ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(song.name, fontWeight = FontWeight.Bold)

                    Row {
                        if (!isEditingSongs) {
                            if (!song.linkSpotify.isNullOrEmpty()) {
                                Icon(
                                    painter = painterResource(id = R.drawable.spotify_icon),
                                    contentDescription = "Spotify",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(song.linkSpotify))
                                            navController.context.startActivity(intent)
                                        }
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            if (!song.linkYT.isNullOrEmpty()) {
                                Icon(
                                    painter = painterResource(id = R.drawable.youtube_icon),
                                    contentDescription = "YouTube",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(song.linkYT))
                                            navController.context.startActivity(intent)
                                        }
                                )
                            }
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.editing_pen),
                                contentDescription = "Edit Song",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        selectedSongForEdit = song
                                        isEditSongDialogVisible = true
                                    }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Icon(
                                painter = painterResource(R.drawable.delete_items),
                                contentDescription = "Delete Song",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        viewModel.deleteSong(song.id)
                                        albumSongs = albumSongs.filter { it.id != song.id }
                                    }
                            )
                        }
                    }
                }
            }
        }

        if (isEditAlbumDialogVisible) {
            UpdateAlbumDialog(
                album = album,
                isVisible = isEditAlbumDialogVisible,
                onDismiss = { isEditAlbumDialogVisible = false },
                onUpdate = { updatedAlbum ->
                    Log.d("UpdateAlbum", "Sending updated album: $updatedAlbum")
                    viewModel.updateAlbum(updatedAlbum)
                    isEditAlbumDialogVisible = false
                    albumSongs = updatedAlbum.existingSongs
                    viewModel.fetchAlbums(album.musicianId)
                }
            )
        }

        if (isEditSongDialogVisible && selectedSongForEdit != null) {
            UpdateSongDialog(
                song = selectedSongForEdit!!,
                isVisible = isEditSongDialogVisible,
                onDismiss = { isEditSongDialogVisible = false },
                onUpdate = { updatedSong ->
                    viewModel.updateSong(updatedSong)
                    isEditSongDialogVisible = false
                    albumSongs = albumSongs.map { if (it.id == updatedSong.id) updatedSong else it }
                }
            )
        }
    }
}

