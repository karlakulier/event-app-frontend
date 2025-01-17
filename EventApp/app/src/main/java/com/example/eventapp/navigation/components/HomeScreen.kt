package com.example.eventapp.navigation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Logo", fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
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
                .padding(horizontal = 16.dp)
        ) {
            //Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp), // Adjust padding if necessary
                contentAlignment = Alignment.CenterEnd // Aligns content to the right
            ) {
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
            SearchBarHome()
            Spacer(modifier = Modifier.height(16.dp))
            ActiveEventsSection(navController)
        }
    }
}



@Composable
fun SearchBarHome() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray.copy(alpha = 0.3f), shape = CircleShape)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.Search, contentDescription = "Search Icon")
    }
}

@Composable
fun ActiveEventsSection(navController: Any) {
    Text("ACTIVE EVENTS", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
    repeat(3) {
        EventCard("Name of event and Date")
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun EventCard(eventTitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray.copy(alpha = 0.2f))
            .padding(16.dp)
    ) {
        Text(eventTitle, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color.Gray.copy(alpha = 0.5f))
        )
    }
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Icon(Icons.Default.Star, contentDescription = "Favorites")
        Icon(Icons.Default.Home, contentDescription = "Home")
        Icon(Icons.Default.Refresh, contentDescription = "History")
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


