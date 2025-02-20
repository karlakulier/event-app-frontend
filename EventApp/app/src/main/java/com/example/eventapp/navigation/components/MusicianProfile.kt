package com.example.eventapp.navigation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ws.services.MusicianService
import kotlinx.coroutines.launch
import com.example.core.network.models.MusicianProfile



@Composable
fun MusicianProfile(
    musicianId: Int,
    musicianService: MusicianService,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var artistName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(musicianId) {
        val musician = musicianService.getMusicianById(musicianId)
        if (musician != null) {
            name = musician.name
            surname = musician.surname
            email = musician.email
            contact = musician.contact
            artistName = musician.artistName ?: ""
            description = musician.description ?: ""
        }
    }

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        // Header (umjesto Scaffold topBar)
        Header(navController)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Edit Profile",
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 24.dp),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000000)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProfileEditFields("Name", name) { name = it }
        ProfileEditFields("Surname", surname) { surname = it }
        ProfileEditFields("Email", email) { email = it }
        ProfileEditFields("Contact", contact) { contact = it }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    navController.navigate("musicianProfileViewOnly/$musicianId") {
                        popUpTo("musicianProfile/$musicianId") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    val updatedProfile = MusicianProfile(
                        id = musicianId,
                        name = name,
                        surname = surname,
                        email = email,
                        contact = contact,
                        artistName = artistName,
                        description = description,
                        userId = musicianId
                    )
                    coroutineScope.launch {
                        val result = musicianService.updateMusicianProfile(musicianId, updatedProfile)
                        dialogMessage = "Podaci su uspješno ažurirani!"
                        showDialog = true
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text("Save")
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    navController.navigate("musicianProfileViewOnly/$musicianId") {
                        popUpTo("musicianProfile/$musicianId") { inclusive = true }
                    }
                },
                title = { Text("Profile Update") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            navController.navigate("musicianProfileViewOnly/$musicianId") {
                                popUpTo("musicianProfile/$musicianId") { inclusive = true }
                            }
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        ClassicFooter(navController)
    }
}


@Composable
fun ProfileEditFields(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "$label:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            label = { Text("") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

