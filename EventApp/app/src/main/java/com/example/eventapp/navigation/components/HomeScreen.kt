package com.example.eventapp.navigation.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.eventapp.R
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.example.core.login.Auth
import com.example.eventapp.viewmodels.GetEventViewModel
import com.example.ws.models.GetEventData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.air.eventapplication.ApplicationModul
import com.example.eventapp.viewmodels.EventSearchViewModel
import com.example.eventapp.viewmodels.EventSearchViewModelFactory
import com.example.eventapp.viewmodels.GetEventViewModelFactory
import com.example.ws.repository.EventRepository
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: GetEventViewModel = viewModel(factory = GetEventViewModelFactory(EventRepository()))
) {
    val searchViewModel: EventSearchViewModel = viewModel(factory = EventSearchViewModelFactory(EventRepository()))
    val query by searchViewModel.query.collectAsState()
    val searchResults by searchViewModel.searchResults.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var isFilterDialogVisible by remember { mutableStateOf(false) }
    var hasActiveFilters by remember { mutableStateOf(false) }
    val loggedInUserRole = remember { Auth.loggedInUserData?.role }

    LaunchedEffect(Unit) {
        viewModel.fetchSortedEvents("relevance")
    }

    Scaffold(
        topBar = { Header(navController) },
        bottomBar = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
            ) {
                if (loggedInUserRole == "Organizer") {
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
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.Start) {
                                Text(
                                    text = "Filter",
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .clickable { isFilterDialogVisible = true }
                                        .padding(end = 16.dp)
                                )
                                if (hasActiveFilters) {
                                    Icon(
                                        painter = painterResource(R.drawable.abandon_edit),
                                        contentDescription = "Clear Filters",
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable {
                                                hasActiveFilters = false
                                                viewModel.fetchEvents()
                                            }
                                    )
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Sort by:",
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(end = 2.dp)
                                )
                                SortByDropdown()
                            }
                        }
                    }
                    CustomSearchBar(
                        query = query,
                        onQueryChanged = { searchViewModel.onQueryChanged(it) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    val eventsToShow = if (query.isEmpty()) viewModel.events.observeAsState(emptyList()).value else searchResults


                    if (eventsToShow.isEmpty()) {
                        Text(
                            text = "No events found.",
                            modifier = Modifier.padding(16.dp),
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    } else {
                        LazyColumn {
                            items(eventsToShow) { event ->
                                EventCard(event, navController)
                            }
                        }
                    }
                }
            }
        }

        if (isFilterDialogVisible) {
            FilterDialog(
                isVisible = isFilterDialogVisible,
                onDismiss = { isFilterDialogVisible = false },
                onApplyFilter = { location, date, genres ->
                    hasActiveFilters = true
                    viewModel.fetchFilteredEvents(location, date, genres)
                },
                viewModel = viewModel
            )
        }
    }
}

data class Event(
    val id: String,
    val name: String,
    val date: String,
    val imageUrl: String
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActiveEventsSection(
    navController: NavController,
    viewModel: GetEventViewModel = viewModel()
) {
    val events by viewModel.events.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchEvents()
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(events) { event ->
            EventCard(event, navController)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventCard(event: GetEventData, navController: NavController) {
    val loggedUser = Auth.loggedInUserData
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate("eventDetailsGlobalForOrganizer/${event.id}")
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://via.placeholder.com/300"),
                contentDescription = "Event Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            if (loggedUser?.role == "Musician" && event.finished==false) {
                Button(
                    onClick = {
                        ApplicationModul.SendApplication(context,event.id,loggedUser.musician?.id!!)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "Submit application", fontSize = 12.sp, color = Color.Black)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp)
        ) {
            Text(
                text = event.description,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = "Location: " + event.location,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.Black
            )
            Text(
                text = "Music genre: " + event.musicGenre,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.Black
            )
            Text(
                text = "Date and time: " + formatDateTime(event.dateTime),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}



@Composable
fun SortByDropdown(viewModel: GetEventViewModel = viewModel()) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Relevance") }

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopEnd)
    ) {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.3f),
                contentColor = Color.Black
            ),
            shape = MaterialTheme.shapes.small,
            contentPadding = PaddingValues(horizontal = 8.dp),
            modifier = Modifier.wrapContentSize()
        ) {
            Text(selectedOption, fontSize = 14.sp)
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown Arrow")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .wrapContentSize()
        ) {
            DropdownMenuItem(
                text = { Text("Relevance") },
                onClick = {
                    selectedOption = "Relevance"
                    expanded = false
                    viewModel.fetchSortedEvents("relevance")
                }
            )
            DropdownMenuItem(
                text = { Text("Date from newest") },
                onClick = {
                    selectedOption = "Date from newest"
                    expanded = false
                    viewModel.fetchSortedEvents("newest")
                }
            )
            DropdownMenuItem(
                text = { Text("Date from oldest") },
                onClick = {
                    selectedOption = "Date from oldest"
                    expanded = false
                    viewModel.fetchSortedEvents("oldest")
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(dateTimeString: String): String {
    val formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val formatterOutput = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    return try {
        val dateTime = LocalDateTime.parse(dateTimeString, formatterInput)
        dateTime.format(formatterOutput)
    } catch (e: Exception) {
        Log.e("DateFormatter", "Error parsing date: ${e.message}")
        "Invalid date"
    }
}


