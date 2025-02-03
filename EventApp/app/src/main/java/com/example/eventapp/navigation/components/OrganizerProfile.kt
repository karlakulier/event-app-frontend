package com.example.eventapp.navigation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventapp.services.OrganizerService
import com.example.eventapp.models.OrganizerProfile
import com.example.eventapp.services.OrganizerRetrofit
import kotlinx.coroutines.launch

@Composable
fun OrganizerProfile(
    organizerId: Int,
    organizerService: OrganizerService,

) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var review by remember { mutableStateOf("") }  // Dodano review polje
    var isEditing by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    // Dohvat podataka sa servera
    LaunchedEffect(organizerId) {
        val organizer = organizerService.getOrganizerById(organizerId)
        if (organizer != null) {
            name = organizer.name
            surname = organizer.surname
            email = organizer.email
            contact = organizer.contact
            birthDate = organizer.birthDate
            review = organizer.review ?: ""  // Dodajemo review
        }
    }

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Edit Profile", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6200EE))

        Spacer(modifier = Modifier.height(16.dp))

        // Prikaz editable form
        ProfileEditField("Name", name) { name = it }
        ProfileEditField("Surname", surname) { surname = it }
        ProfileEditField("Email", email) { email = it }
        ProfileEditField("Contact", contact) { contact = it }
        ProfileEditField("Birth Date", birthDate) { birthDate = it }
        ProfileEditField("Review", review) { review = it }  // Dodajemo polje za unos review-a

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {
                // Spremi ažurirane podatke
                val updatedProfile = OrganizerProfile(
                    id = organizerId, // Postavite ID koji je dohvaćen
                    name = name,
                    surname = surname,
                    email = email,
                    contact = contact,
                    birthDate = birthDate,
                    review = review,
                    userId = organizerId // Ako koristite isti ID kao za organizatora, postavite ga
                )
                coroutineScope.launch {
                    // Poziv za ažuriranje profila
                    val result = organizerService.updateOrganizerProfile(organizerId, updatedProfile)
                    dialogMessage = "Profile updated successfully!"

                }
            }) {
                Text("Save", color = Color.White)
            }

            Button(onClick = { isEditing = false }) {
                Text("Cancel")
            }
        }
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Profile Update") },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun ProfileEditField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "$label:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun PreviewOrganizerProfile() {
    val organizerService = OrganizerService(OrganizerRetrofit.api)
    val organizerId = 1
    OrganizerProfile(organizerId = organizerId, organizerService = organizerService)
}
