package com.example.eventapp.navigation.components

import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.add_album_ep_single.MusicContentType
import com.example.core.login.Auth
import com.example.eventapp.R
import com.example.eventapp.viewmodels.ArtistProfileViewModel
import com.example.ws.models.AlbumData
import com.example.ws.models.EpData
import com.example.ws.models.SongData


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistProfileScreen(navController: NavController, viewModel: ArtistProfileViewModel) {
    val isOrganizer = Auth.loggedInUserData?.role == "Organizer"
    var selectedItemForEdit by remember { mutableStateOf<Any?>(null) }
    var isEditDialogVisible by remember { mutableStateOf(false) }
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

    var artistName by remember { mutableStateOf(Auth.loggedInUserData?.musician?.artistName ?: "Unknown Artist") }

    LaunchedEffect(Auth.loggedInUserData?.musician?.artistName) {
        artistName = Auth.loggedInUserData?.musician?.artistName ?: "Unknown Artist"
    }

    Scaffold(
        topBar = {
            Header(navController)
        },
        bottomBar = {
            Column {
                HorizontalDivider(
                    color = Color.Black,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                if (isOrganizer) {
                    OrganizerFooter(navController = navController)
                } else {
                    ClassicFooter(navController = navController)
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .border(1.dp, Color.DarkGray)
                    .height(300.dp)
            ) {
                if (currentProfileImage != null) {
                    AsyncImage(
                        model = currentProfileImage,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_photo_musician),
                        contentDescription = "Default Profile Image",
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.Center)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomStart)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                        text = artistName ?: "Unknown Artist",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier.weight(1f)
                    )
                    }
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
                                launcher.launch("image/*")
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    if (isEditing) {
                        Image(
                            painter = painterResource(R.drawable.approve_edit),
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
                                }
                        )
                        Image(
                            painter = painterResource(R.drawable.abandon_edit),
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
                                }
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
                                .clickable { isEditing = true }
                        )
                    }
                }
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.concert_musician_profile),
                        contentDescription = "Concerts",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { navController.navigate("pastEventsForMusician") }
                    )
                    Image(
                        painter = painterResource(R.drawable.profile_info_musician),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(45.dp)
                            .clickable {
                                val userRole = Auth.loggedInUserData?.role
                                Log.d("Navigation", "User role: $userRole")
                                val destination = if (userRole == "Musician") {
                                    "musicianProfileViewOnly/${Auth.loggedInUserData?.musician?.id}"
                                } else {
                                    "artistProfileScreen"
                                }
                                navController.navigate(destination)
                            }
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

            selectedItemForEdit?.let { item ->
                when (item) {
                    is AlbumData -> UpdateAlbumDialog(
                        isVisible = isEditDialogVisible,
                        album = item,
                        onDismiss = { selectedItemForEdit = null; isEditDialogVisible = false },
                        onUpdate = { updatedAlbum -> viewModel.updateAlbum(updatedAlbum) }
                    )
                    is EpData -> UpdateEPDialog(
                        isVisible = isEditDialogVisible,
                        ep = item,
                        onDismiss = { selectedItemForEdit = null; isEditDialogVisible = false },
                        onUpdate = { updatedEp -> viewModel.updateEp(updatedEp) }
                    )
                    is SongData -> UpdateSongDialog(
                        isVisible = isEditDialogVisible,
                        song = item,
                        onDismiss = { selectedItemForEdit = null; isEditDialogVisible = false },
                        onUpdate = { updatedSong -> viewModel.updateSong(updatedSong) }
                    )
                }
            }
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
                0 -> AlbumList(
                    albums = albums,
                    isEditing = isEditing,
                    viewModel = viewModel,
                    navController = navController,
                    onEdit = { selectedItemForEdit = it; isEditDialogVisible = true },
                    onDelete = { viewModel.deleteAlbum(it.id) }
                )
                1 -> EPList(
                    eps = eps,
                    isEditing = isEditing,
                    viewModel = viewModel,
                    navController = navController,
                    onEdit = { selectedItemForEdit = it; isEditDialogVisible = true },
                    onDelete = { viewModel.deleteEp(it.id) }
                )
                2 -> SingleList(
                    songs = songs,
                    isEditing = isEditing,
                    viewModel = viewModel,
                    navController = navController,
                    onEdit = { selectedItemForEdit = it; isEditDialogVisible = true },
                    onDelete = { viewModel.deleteSong(it.id) }
                )
            }
        }
    }
}

@Composable
fun AlbumList(
    albums: List<AlbumData>,
    isEditing: Boolean,
    viewModel: ArtistProfileViewModel,
    navController: NavController,
    onEdit: (AlbumData) -> Unit,
    onDelete: (AlbumData) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(albums) { album ->
            MusicCard(album, isEditing, viewModel, navController, onEdit, onDelete)
        }
    }
}

@Composable
fun EPList(
    eps: List<EpData>,
    isEditing: Boolean,
    viewModel: ArtistProfileViewModel,
    navController: NavController,
    onEdit: (EpData) -> Unit,
    onDelete: (EpData) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(eps) { ep ->
            MusicCard(ep, isEditing, viewModel, navController, onEdit, onDelete)
        }
    }
}

@Composable
fun SingleList(
    songs: List<SongData>,
    isEditing: Boolean,
    viewModel: ArtistProfileViewModel,
    navController: NavController,
    onEdit: (SongData) -> Unit,
    onDelete: (SongData) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(songs) { song ->
            MusicCard(song, isEditing, viewModel, navController, onEdit, onDelete)
        }
    }
}

@Composable
fun <T> MusicCard(
    item: T,
    isEditing: Boolean,
    viewModel: ArtistProfileViewModel,
    navController: NavController,
    onEdit: (T) -> Unit,
    onDelete: (T) -> Unit
) {
    val itemName = when (item) {
        is AlbumData -> item.name
        is EpData -> item.name
        is SongData -> item.name
        else -> "Unknown"
    }

    val context = LocalContext.current

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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(itemName, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!isEditing) {
                    if (item is SongData) {
                        if (!item.linkSpotify.isNullOrEmpty()) {
                            Icon(
                                painter = painterResource(id = R.drawable.spotify_icon),
                                contentDescription = "Open Spotify",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.linkSpotify))
                                        context.startActivity(intent)
                                    }
                            )
                        }

                        if (!item.linkYT.isNullOrEmpty()) {
                            Icon(
                                painter = painterResource(id = R.drawable.youtube_icon),
                                contentDescription = "Open YouTube",
                                modifier = Modifier
                                    .size(30.dp)
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.linkYT))
                                        context.startActivity(intent)
                                    }
                            )
                        }
                    }
                }
                if (!isEditing) {
                    if (item is AlbumData) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = "Open Album",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    navController.navigate("albumDetails/${item.id}")
                                }
                        )
                    } else if (item is EpData) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = "Open EP",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    navController.navigate("epDetails/${item.id}")
                                }
                        )
                    }
                }
                if (isEditing) {
                    Icon(
                        painter = painterResource(R.drawable.editing_pen),
                        contentDescription = "Edit $itemName",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onEdit(item) }
                    )

                    Icon(
                        painter = painterResource(R.drawable.delete_items),
                        contentDescription = "Delete $itemName",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onDelete(item) }
                    )
                }
            }
        }
    }
}