package com.example.eventapp.navigation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eventapp.ui.theme.ProfileField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviousEvents(navController: NavController) {
    Scaffold(
        topBar = {
          Header()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(16.dp)
                )
                Text(text = "NAME AND SURNAME", style = MaterialTheme.typography.bodyMedium)
                Text(text = "role", style = MaterialTheme.typography.bodyMedium)
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ProfileField(label = "Name", value = "Ana")
                ProfileField(label = "Surname", value = "Anić")
                ProfileField(label = "E-mail address", value = "aanic31@gmail.com", icon = Icons.Default.Email)
                ProfileField(label = "Contact", value = "+098577876578", icon = Icons.Default.Phone)
                ProfileField(label = "Date of birth", value = "23/09/1990", icon = Icons.Default.CalendarToday)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Previous events button
            Button(
                onClick = { navController.navigate("previousEvents") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE6E6E6)
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Previous events",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Arrow",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}
