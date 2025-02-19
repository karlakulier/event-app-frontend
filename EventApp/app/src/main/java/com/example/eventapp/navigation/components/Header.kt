package com.example.eventapp.navigation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.core.login.Auth
import com.example.eventapp.R


@Composable
fun Header(navController: NavController) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.group),
                    contentDescription = "Logo",
                    modifier = Modifier.size(200.dp), // Povećana veličina
                    contentScale = ContentScale.Fit
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                val userRole = Auth.loggedInUserData?.role
                val destination = if (userRole == "Organizer") {
                    "organizerProfileViewOnly/${Auth.loggedInUserData?.organizer?.id}"
                } else {
                    "artistProfileScreen"
                }
                navController.navigate(destination)
            }) {
                Icon(Icons.Default.Person, contentDescription = "User Icon", modifier = Modifier.padding(start = 16.dp))
            }
        },
        actions = {
            IconButton(onClick = { /* Dodaj akciju za meni */ }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu Icon", modifier = Modifier.padding(end = 16.dp))
            }
        },
        backgroundColor = Color.Transparent, // Postavljanje pozadine kao transparentne
        elevation = 0.dp // Uklanjanje sjene i obruba
    )
}




@Composable
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = "Search...", color = Color.Gray)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = "Filter",
            modifier = Modifier.size(24.dp)
        )
    }
}



