package com.example.eventapp.navigation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var isFilterDialogVisible by remember { mutableStateOf(false) }
    var hasActiveFilters by remember { mutableStateOf(false) }


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
                    IconButton(onClick = {
                        navController.navigate("artistProfileScreen") // Navigacija na ArtistProfileScreen
                    }) {
                        Icon(Icons.Default.Person, contentDescription = "User Icon", modifier = Modifier.padding(start = 16.dp))
                        }
                    },
                actions = {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu Icon",
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            BottomNavigationBar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .drawBehind {
                    // Gornji border
                    drawLine(
                        color = Color.Black,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                    // Donji border
                    drawLine(
                        color = Color.Black,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2.dp.toPx()
                    )
                }
                .padding(horizontal = 16.dp)
        ) {
            //Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp), // Adjust padding if necessary
                contentAlignment = Alignment.CenterEnd // Aligns content to the right
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) { Row(
                    horizontalArrangement = Arrangement.Start,
                ){
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
                            painter = painterResource(R.drawable.abandon_edit), // Zamijeni s pravim resursom
                            contentDescription = "Clear Filters",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    hasActiveFilters = false // Resetiranje filtera
                                    // Dodajte logiku za uklanjanje primijenjenih filtera
                                    println("Filters reset")
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
                query = searchQuery,
                onQueryChanged = { searchQuery = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ActiveEventsSection(navController)
        }
        if (isFilterDialogVisible) {
            FilterDialog(
                isVisible = isFilterDialogVisible,
                onDismiss = { isFilterDialogVisible = false },
                onApplyFilter = { location, date, genres ->
                    hasActiveFilters = true
                    // Logika za primjenu filtera
                    println("Filter applied: Location=$location, Date=$date, Genres=$genres")
                }
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

@Composable
fun ActiveEventsSection(navController: NavController) {
    val mockEvents = listOf(
        Event("1", "Music Festival", "2023-06-01", "https://via.placeholder.com/300"),
        Event("2", "Art Exhibition", "2023-07-15", "https://via.placeholder.com/300"),
        Event("3", "Tech Conference", "2023-08-20", "https://via.placeholder.com/300")
    )

    Text(
        text = "ACTIVE EVENTS",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(mockEvents) { event ->
            EventCard(event, navController)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun EventCard(event: Event, navController: NavController) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                // Navigacija na detalje događaja
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            // Prikaz slike eventa
            Image(
                painter = rememberAsyncImagePainter(event.imageUrl),
                contentDescription = "Event Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gumb za prijavu
            Button(
                onClick = {
                    // Navigacija na EventInputScreen
                    navController.navigate("eventInputScreen") // Ako ste postavili rutu za EventInputScreen
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(text = "Submit application", fontSize = 12.sp, color = Color.Black)
            }
        }

        // Naziv i datum eventa
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp)
        ) {
            Text(
                text = event.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = event.date,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

fun submitApplication(eventId: String) {
    // Ovdje možete implementirati logiku za prijavu na event
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.favourites),
            contentDescription = "Favorites",
            modifier = Modifier
                .size(35.dp)
                .clickable{}
        )
        Icon(
            painter = painterResource(id = R.drawable.home),
            contentDescription = "Home",
            modifier = Modifier
                .size(35.dp)
                .clickable{}
        )
        Icon(
            painter = painterResource(id = R.drawable.chat_bubble),
            contentDescription = "History",
            modifier = Modifier
                .size(35.dp)
                .clickable{}
        )
    }
}

@Composable
fun SortByDropdown() {
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
                }
            )
            DropdownMenuItem(
                text = { Text("Date from newest") },
                onClick = {
                    selectedOption = "Date from newest"
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Date from oldest") },
                onClick = {
                    selectedOption = "Date from oldest"
                    expanded = false
                }
            )
        }
    }
}


