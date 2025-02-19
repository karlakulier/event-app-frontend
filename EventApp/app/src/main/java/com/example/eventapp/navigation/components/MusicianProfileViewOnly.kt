package com.example.eventapp.navigation.components

import java.text.SimpleDateFormat
import java.util.*

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.core.login.Auth
import com.example.ws.network.MusicianRetrofit
import com.example.ws.services.MusicianService

@Composable
fun MusicianProfileViewOnly(
    musicianId: Int,
    musicianService: MusicianService,
    navController: NavController,
    onEditClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }

    // Dohvati podatke organizatora
    LaunchedEffect(musicianId) {
        val musician = musicianService.getMusicianById(musicianId)
        if (musician != null) {
            name = musician.name
            surname = musician.surname
            email = musician.email
            contact = musician.contact
        } else {
            Log.d("MusicianProfile", "No data found for musicianId: $musicianId")
        }
    }

    // Provjeri je li prijavljeni organizator isti kao onaj koji se prikazuje
    val isLoggedInMusician = Auth.loggedInUserData?.musician?.id == musicianId
    Log.d("MusicianProfile", "Logged-in user ID: ${Auth.loggedInUserData?.id}, Musician ID: $musicianId")
    Log.d("MusicianProfile", "isLoggedInMusician: $isLoggedInMusician")

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Profile", fontSize = 30.sp, modifier = Modifier.padding(bottom = 24.dp), fontWeight = FontWeight.Bold, color = Color(0xFF000000))

        Spacer(modifier = Modifier.height(16.dp))

        ProfileDetailFields("Name", name)
        ProfileDetailFields("Surname", surname)
        ProfileDetailFields("Email", email)
        ProfileDetailFields("Contact", contact)

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoggedInMusician) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                Button(onClick = { onEditClick() }) {
                    Text("Edit Profile")
                }

                Button(onClick = { navController.navigate("artistProfileScreen")}) {
                    Text("Back")
                }

            }
        } else {
            Text("You do not have permission to edit this profile.", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ProfileDetailFields(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "$label:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = value,
            onValueChange = {},
            label = { Text("") },
            enabled = false,
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
