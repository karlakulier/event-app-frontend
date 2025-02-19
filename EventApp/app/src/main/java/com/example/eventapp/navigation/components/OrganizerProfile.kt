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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ws.services.OrganizerService
import com.example.core.network.models.OrganizerProfile
import com.example.ws.network.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun OrganizerProfile(
    organizerId: Int,
    organizerService: OrganizerService,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var review by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    // Dohvati podatke o organizatoru
    LaunchedEffect(organizerId) {
        val organizer = organizerService.getOrganizerById(organizerId)
        if (organizer != null) {
            name = organizer.name
            surname = organizer.surname
            email = organizer.email
            contact = organizer.contact
            birthDate = organizer.birthDate
            review = organizer.review ?: ""
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Edit Profile",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6200EE)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProfileEditField("Name", name) { name = it }
        ProfileEditField("Surname", surname) { surname = it }
        ProfileEditField("Email", email) { email = it }
        ProfileEditField("Contact", contact) { contact = it }
        ProfileEditField("Birth Date", birthDate) { birthDate = it }
        ProfileEditField("Review", review) { review = it }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    navController.navigate("organizerProfileViewOnly/$organizerId") {
                        popUpTo("organizerProfile/$organizerId") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    val updatedProfile = OrganizerProfile(
                        id = organizerId,
                        name = name,
                        surname = surname,
                        email = email,
                        contact = contact,
                        birthDate = birthDate,
                        review = review,
                        userId = organizerId
                    )
                    coroutineScope.launch {
                        val result = organizerService.updateOrganizerProfile(organizerId, updatedProfile)
                        dialogMessage = "Podaci su uspješno ažurirani!"
                        showDialog = true
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text("Save")
            }
        }
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                navController.navigate("organizerProfileViewOnly/$organizerId") {
                    popUpTo("organizerProfile/$organizerId") { inclusive = true }
                }
            },
            title = { Text("Profile update") },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        navController.navigate("organizerProfileViewOnly/$organizerId") {
                            popUpTo("organizerProfile/$organizerId") { inclusive = true }
                        }
                    }
                ) {
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
    val organizerService = OrganizerService(RetrofitClient.organizerApi)
    val organizerId = 1
    val navController = rememberNavController()
    OrganizerProfile(organizerId = organizerId, organizerService = organizerService, navController = navController)
}
