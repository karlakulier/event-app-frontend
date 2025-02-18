package com.example.eventapp.navigation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.add_album_ep_single.MusicContentType
import com.example.add_album_ep_single.MusicService
import com.example.core.login.Auth
import com.example.eventapp.R
import com.example.eventapp.viewmodels.ArtistProfileViewModel
import com.example.eventapp.viewmodels.ArtistProfileViewModelFactory
import com.example.ws.models.AlbumData
import com.example.ws.models.EpData
import com.example.ws.models.SongData
import com.example.ws.network.RetrofitClient.apiArtistProfile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistProfileScreen(navController: NavController, viewModel: ArtistProfileViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Albums", "EPs", "Singles")
    var searchQuery by remember { mutableStateOf("") }
    var isDialogVisible by remember { mutableStateOf(false) }
    var isAddDialogVisible by remember { mutableStateOf(false) }
    var selectedContentType by remember { mutableStateOf<MusicContentType?>(null) }
    var isAlbumDialogVisible by remember { mutableStateOf(false) }
    var isEpDialogVisible by remember { mutableStateOf(false) }
    var isSongDialogVisible by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    var isEditModeEnabled by remember { mutableStateOf(false) }
    var newArtistName by remember { mutableStateOf("ARTIST NAME") }
    var originalArtistName by remember { mutableStateOf(newArtistName) }
    var selectedGenre by remember { mutableStateOf("GENRE") }
    var originalGenre by remember { mutableStateOf(selectedGenre) }
    var isGenreDialogVisible by remember { mutableStateOf(false) }
    var currentProfileImage by remember { mutableStateOf<Uri?>(null) }
    var newProfileImage by remember { mutableStateOf<Uri?>(null) }
    var isEditingImage by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            newProfileImage = uri
        }
    }
    val albums by viewModel.albums.collectAsState()
    val eps by viewModel.eps.collectAsState()
    val songs by viewModel.songs.collectAsState()
    Auth.loggedInUserData?.musician?.id?.let { musicianId ->
        LaunchedEffect(Unit) {
            viewModel.fetchAlbums(musicianId)
            viewModel.fetchEPs(musicianId)
            viewModel.fetchSongs(musicianId)
        }
    }

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
                    .border(1.dp, Color.DarkGray)
                    .height(300.dp)
            ) {
                // Prikaz slike ako je postavljena
                if (currentProfileImage != null) {
                    AsyncImage(
                        model = currentProfileImage,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop // Slika ispunjava prostor
                    )
                } else {
                    // Prikaz ikone ako slika nije postavljena
                    Icon(
                        painter = painterResource(id = R.drawable.profile_photo_musician),
                        contentDescription = "Default Profile Image",
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.Center)
                    )
                }

                // Search bar unutar sive pozadine na vrhu
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                        .padding(16.dp) // Margina oko search bara
                ) {
                    CustomSearchBar(
                        query = searchQuery,
                        onQueryChanged = { searchQuery = it }
                    )
                }

                // Tekst i elementi na dnu slike
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomStart) // Poravnanje na dno sive pozadine
                ) {
                    // Artist Name
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isEditing) {
                            if (isEditModeEnabled) {
                                OutlinedTextField(
                                    value = newArtistName,
                                    onValueChange = { newArtistName = it },
                                    label = { Text("Edit Name") },
                                    modifier = Modifier.weight(1f).padding(4.dp)
                                )
                            } else {
                                Text(
                                    text = newArtistName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    modifier = Modifier.padding(end = 10.dp)
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.editing_pen),
                                    contentDescription = "Edit Artist Name",
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clickable {
                                            originalArtistName = newArtistName
                                            isEditModeEnabled = true
                                        }
                                )
                            }
                        } else {
                            Text(
                                text = newArtistName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Genre
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedGenre,
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(end = 5.dp)
                        )

                        if (isEditing) {
                            Icon(
                                painter = painterResource(id = R.drawable.editing_pen),
                                contentDescription = "Edit Genre",
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(20.dp)
                                    .clickable {
                                        originalGenre = selectedGenre
                                        isGenreDialogVisible = true
                                    }
                            )
                        }
                    }

                    if (isGenreDialogVisible) {
                        GenreSelectionDialog(
                            selectedGenre = selectedGenre,
                            onGenreSelected = { genre ->
                                selectedGenre = genre
                            },
                            onDismiss = {
                                isGenreDialogVisible = false
                            }
                        )
                    }
                }

                // Dodavanje ikone za uređivanje slike u donji desni kut
                if (isEditing) {
                    Icon(
                        painter = painterResource(id = R.drawable.editing_pen),
                        contentDescription = "Edit Profile Image",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .size(30.dp)
                            .clickable { isEditingImage = true }
                    )
                }
            }


            if (isEditingImage) {
                AlertDialog(
                    onDismissRequest = { isEditingImage = false },
                    confirmButton = {
                        TextButton(onClick = {
                            if (newProfileImage != null) {
                                currentProfileImage = newProfileImage
                            }
                            isEditingImage = false
                        }) {
                            Text("Approve")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            newProfileImage = null
                            isEditingImage = false
                        }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Upload New Profile Image") },
                    text = {
                        Column {
                            Button(onClick = {
                                launcher.launch("image/*") // Pokreće odabir slike
                            }) {
                                Text("Select Image")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            newProfileImage?.let {
                                Text("Selected Image: ${it.lastPathSegment}")
                            }
                        }
                    }
                )
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
                                .clickable {
                                    isEditing = false
                                    isEditModeEnabled = false
                                    isGenreDialogVisible = false
                                    if (newProfileImage != null) {
                                        currentProfileImage = newProfileImage
                                    }
                                } // Završava uređivanje
                        )
                        Image(
                            painter = painterResource(R.drawable.abandon_edit), // Ikona za odustajanje
                            contentDescription = "Cancel Changes",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    selectedGenre = originalGenre
                                    newArtistName = originalArtistName
                                    isEditing = false
                                    isEditModeEnabled = false
                                    isGenreDialogVisible = false
                                    newProfileImage = null
                                    currentProfileImage = null
                                } // Odbacuje promjene
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
                    onDismiss = {isDialogVisible = false},
                    onContentSelected = {selectedContentType = it}
                )
            }

            selectedContentType?.let { contentType ->
                when (contentType) {
                    MusicContentType.ALBUM -> AddNewAlbumDialog(
                        isVisible = true,
                        onDismiss = { selectedContentType = null },
                        onAddAlbum = { albumName, releaseDate, songs ->
                            try {
                                val albumData = AlbumData(
                                    id = 0,
                                    name = albumName,
                                    releaseDate = releaseDate,
                                    musicianId = Auth.loggedInUserData?.musician?.id ?: 0,
                                    existingSongs = listOf(),
                                    newSongs = songs.map { song ->
                                        SongData(
                                            id = 0,
                                            name = song["name"] ?: "",
                                            linkYT = song["youtubeLink"] ?: "",
                                            linkSpotify = song["spotifyLink"] ?: "",
                                            musicianId = Auth.loggedInUserData?.musician?.id ?: 0,
                                            albumId = 0,
                                            epId = 0
                                        )
                                    }
                                )
                                viewModel.addAlbum(albumData)
                            }catch (e: Exception) {
                                println("Greška pri dodavanju albuma: ${e.message}")
                            }
                        }
                    )
                    MusicContentType.EP -> AddNewEPDialog(
                        isVisible = true,
                        onDismiss = { selectedContentType = null },
                        onAddEp = { epName, releaseDate, songs ->
                            try {
                                val epData = EpData(
                                    id = 0,
                                    name = epName,
                                    releaseDate = releaseDate,
                                    musicianId = Auth.loggedInUserData?.musician?.id ?: 0,
                                    existingSongs = listOf(),
                                    newSongs = songs.map { song ->
                                        SongData(
                                            id = 0,
                                            name = song["name"] ?: "",
                                            linkYT = song["youtubeLink"] ?: "",
                                            linkSpotify = song["spotifyLink"] ?: "",
                                            musicianId = Auth.loggedInUserData?.musician?.id ?: 0,
                                            albumId = 0,
                                            epId = 0
                                        )
                                    }
                                )
                                viewModel.addEp(epData)
                            }catch (e: Exception){
                                println("Greška pri dodavanju ep-a: ${e.message}")
                            }
                        }
                    )
                    MusicContentType.SINGLE -> AddNewSongDialog(
                        isVisible = true,
                        onDismiss = { selectedContentType = null },
                        onAddSong = { songName, youtubeLink, spotifyLink ->

                            val songData = SongData(
                                id = 0,
                                name = songName,
                                linkYT = youtubeLink,
                                linkSpotify = spotifyLink,
                                musicianId = Auth.loggedInUserData?.musician?.id ?: 0,
                                albumId = null,
                                epId = null
                            )
                            viewModel.addSingle(songData)
                        }
                    )
                }
            }


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
                0 -> AlbumList(albums, isEditing)
                1 -> EPList(eps, isEditing)
                2 -> SingleList(songs, isEditing)
            }
        }
    }
}

@Composable
fun AlbumList(albums: List<AlbumData>, isEditing: Boolean) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(albums) { album ->
            MusicCard(itemName = album.name, isEditing = isEditing)
        }
    }
}

@Composable
fun EPList(eps: List<EpData>, isEditing: Boolean) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(eps) { ep ->
            MusicCard(itemName = ep.name, isEditing = isEditing)
        }
    }
}

@Composable
fun SingleList(singles: List<SongData>, isEditing: Boolean) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(singles) { single ->
            MusicCard(itemName = single.name, isEditing = isEditing)
        }
    }
}
@Composable
fun MusicCard(itemName: String, isEditing: Boolean) {
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
            Text(itemName, fontSize = 16.sp)
            if (isEditing) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.editing_pen),
                        contentDescription = "Edit $itemName",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { /* Logika za uređivanje */ }
                    )
                    Icon(
                        painter = painterResource(R.drawable.delete_items),
                        contentDescription = "Delete $itemName",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { /* Logika za brisanje */ }
                    )
                }
            } else {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
            }
        }
    }
}



