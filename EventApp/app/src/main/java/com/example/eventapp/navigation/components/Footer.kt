package com.example.eventapp.navigation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.core.login.Auth.loggedInUserData


@Composable
fun ClassicFooter(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.StarBorder,
            contentDescription = "Favorites",
            modifier = Modifier.size(24.dp).clickable {
                navController.navigate("applicationsForMusician/${loggedInUserData?.musician?.id}")
            }
        )

        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Home",
            modifier = Modifier.size(24.dp).clickable {
                navController.navigate("home")
            }
        )
        Icon(
            imageVector = Icons.Default.ChatBubbleOutline,
            contentDescription = "Message",
            modifier = Modifier.size(24.dp).clickable {
                navController.navigate("chatListScreen")
            }

        )
}
}