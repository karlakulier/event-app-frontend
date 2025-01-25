package com.example.eventapp.navigation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight

@Composable
fun OrganizerProfile() {
    var name by remember { mutableStateOf("Ana") }
    var surname by remember { mutableStateOf("Anić") }
    var email by remember { mutableStateOf("aanic31@gmail.com") }
    var contact by remember { mutableStateOf("+098577876578") }
    var birthDate by remember { mutableStateOf("23/09/1990") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(text = "NAME AND SURNAME", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = "role", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // Name field
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Surname field
        TextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Surname") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // E-mail field
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Contact field
        TextField(
            value = contact,
            onValueChange = { contact = it },
            label = { Text("Contact") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Birth date field
        TextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Date of birth") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { /* Show previous events */ }) {
                Text("Previous events")
            }
            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Button(onClick = { /* Cancel action */ }) {
                    Text("Cancel")
                }
                Button(onClick = { /* Save changes action */ }) {
                    Text("Save Changes")
                }

            }
        }
    }
}
