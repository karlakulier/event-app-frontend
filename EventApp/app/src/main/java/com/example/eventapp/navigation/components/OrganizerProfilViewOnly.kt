package com.example.eventapp.navigation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ViewOnlyProfile() {
    val name = "Ana"
    val surname = "Anić"
    val email = "aanic31@gmail.com"
    val contact = "+098577876578"
    val birthDate = "23/09/1990"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(text = "NAME AND SURNAME", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = "role", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // Name field (view only)
        Text(text = "Name: $name", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        // Surname field (view only)
        Text(text = "Surname: $surname", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        // E-mail field (view only)
        Text(text = "E-mail: $email", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        // Contact field (view only)
        Text(text = "Contact: $contact", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        // Birth date field (view only)
        Text(text = "Date of birth: $birthDate", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Previous events (view only)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { /* Show previous events */ }) {
                Text("Previous events")
            }
        }
    }
}
