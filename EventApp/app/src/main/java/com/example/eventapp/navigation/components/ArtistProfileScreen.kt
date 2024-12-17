package com.example.eventapp.navigation.components

import androidx.compose.foundation.background
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Logo",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
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
                    containerColor = Color.LightGray
                )
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color.LightGray) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* Home action */ }) {
                    Icon(Icons.Default.Home, contentDescription = "Home")
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Search Bar
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // Artist Info
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "Artist Image",
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = "ARTIST NAME",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "Genre",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { /* Add action */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
                IconButton(onClick = { /* Edit action */ }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = { /* View action */ }) {
                    Icon(Icons.Default.AddCircle, contentDescription = "View")
                }
                IconButton(onClick = { /* People action */ }) {
                    Icon(Icons.Default.Edit, contentDescription = "People")
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

            // Content based on selected tab
            LazyColumn(
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
        }
    }
}

