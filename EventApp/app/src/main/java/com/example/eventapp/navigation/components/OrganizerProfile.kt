package com.example.eventapp.navigation.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventapp.models.OrganizerProfile
import com.example.eventapp.services.OrganizerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OrganizerProfile(
    organizerId: Int,
    organizerService: OrganizerService,
    onSaveChanges: (String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(organizerId) {
        val organizer = organizerService.getOrganizerById(organizerId)
        if (organizer != null) {
            name = organizer.name
            surname = organizer.surname
            email = organizer.email
            contact = organizer.contact
            birthDate = organizer.birthDate
        }
    }

    // Display dialog if needed
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Information") },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { /* Cancel action */ }) {
                    Text("Cancel")
                }

                Button(onClick = {
                    onSaveChanges(name, surname, email, contact, birthDate)
                    dialogMessage = "Profile updated successfully!"
                    showDialog = true
                }) {
                    Text("Save Changes")
                }
            }
        }
    }
}
