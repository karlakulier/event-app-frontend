package com.example.eventapp.navigation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.eventapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistProfileScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Albums", "EPs", "Singles")
    var searchQuery by remember { mutableStateOf("") }
    var isDialogVisible by remember { mutableStateOf(false) }
    var isAlbumDialogVisible by remember { mutableStateOf(false) }
    var isEpDialogVisible by remember { mutableStateOf(false) }
    var isSongDialogVisible by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.app_logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(200.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Profile action */ }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Menu action */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Column { // Dodavanje Column za Divider i BottomAppBar
                HorizontalDivider(
                    color = Color.Black, // Crna linija
                    thickness = 1.dp, // Debljina linije
                    modifier = Modifier.fillMaxWidth()
                )
                BottomAppBar(containerColor = Color.White) {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { /* Home action */ }) {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Gray Box with Border and Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .border(1.dp, Color.DarkGray) // Dodaj border (stroke)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    //Search bar
                    CustomSearchBar(
                        query = searchQuery,
                        onQueryChanged = { searchQuery = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Artist Info
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile_photo_musician),
                            contentDescription = "Artist Image",
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.CenterHorizontally) // Ikona ostaje centrirana
                        )
                        Text(
                            text = "ARTIST NAME",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(top = 8.dp).align(Alignment.Start)
                        )
                        Text(
                            text = "Genre",
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(15.dp) // Razmak između ikona
                ) {
                    if (isEditing) {
                        Image(
                            painter = painterResource(R.drawable.approve_edit), // Ikona za potvrdu promjena
                            contentDescription = "Save Changes",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { isEditing = false } // Završava uređivanje
                        )
                        Image(
                            painter = painterResource(R.drawable.abandon_edit), // Ikona za odustajanje
                            contentDescription = "Cancel Changes",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { isEditing = false } // Odbacuje promjene
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.add_musician_profile),
                            contentDescription = "Add",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { isDialogVisible = true }
                        )
                        Image(
                            painter = painterResource(R.drawable.edit_musician_profile),
                            contentDescription = "Edit",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { isEditing = true } // Aktivira uređivanje
                        )
                    }
                }

                // Druga grupa ikona: Concerts i Profile
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(15.dp) // Razmak između ikona
                ) {
                    Image(
                        painter = painterResource(R.drawable.concert_musician_profile),
                        contentDescription = "Concerts",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { /* Concerts action */ }
                    )
                    Image(
                        painter = painterResource(R.drawable.profile_info_musician),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(45.dp)
                            .clickable { navController.navigate("editProfileScreen") }
                    )
                }
            }

            if (isDialogVisible) {
                AddNewAlbumEpSingleDialog(
                    isVisible = isDialogVisible,
                    onDismiss = { isDialogVisible = false },
                    onOptionSelected = { selectedOption ->
                        when (selectedOption) {
                            "Album" -> {
                                isDialogVisible = false // Sakriva prvi dijalog
                                isAlbumDialogVisible = true // Otvara dijalog za dodavanje albuma
                            }
                            "EP" -> {
                                isDialogVisible = false
                                isEpDialogVisible = true
                            }
                            "Single" -> {
                                isDialogVisible = false
                                isSongDialogVisible = true
                            }
                            else -> {
                                println("Odabrano: $selectedOption")
                                isDialogVisible = false // Zatvara dijalog za druge opcije
                            }
                        }
                    }
                )
            }

            AddNewAlbumDialog(
                isVisible = isAlbumDialogVisible,
                onDismiss = { isAlbumDialogVisible = false },
                onAddAlbum = { albumName, releaseDate, songs ->
                    // Handle album creation logic here
                }
            )

            AddNewEPDialog(
                isVisible = isEpDialogVisible,
                onDismiss = {isEpDialogVisible = false },
                onAddEp = {epName, releaseDate, songs ->
                    // Handle EP creation logic here
                }
            )

            AddNewSongDialog(
                isVisible = isSongDialogVisible,
                onDismiss = { isSongDialogVisible = false },
                onAddSong = { songName, releaseDate, youtubeLink, spotifyLink ->
                // Logika za dodavanje pjesme
                }
            )


            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> LazyColumn(
                    modifier = Modifier.padding(8.dp)
                ) {
                    items(listOf("Album 1", "Album 2", "Album 3")) { album ->
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(album, fontSize = 16.sp)
                                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                            }
                        }
                    }
                }

                1 -> LazyColumn(
                    modifier = Modifier.padding(8.dp)
                ) {
                    items(listOf("EP 1", "EP 2", "EP 3")) { ep ->
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(ep, fontSize = 16.sp)
                                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                            }
                        }
                    }
                }

                2 -> LazyColumn(
                    modifier = Modifier.padding(8.dp)
                ) {
                    items(listOf("Single 1", "Single 2", "Single 3")) { single ->
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(single, fontSize = 16.sp)
                                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                            }
                        }
                    }
                }
            }
        }
    }
}

