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
import com.example.core.login.Auth
import com.example.ws.network.OrganizerRetrofit
import com.example.ws.services.OrganizerService

@Composable
fun OrganizerProfileViewOnly(
    organizerId: Int,
    organizerService: OrganizerService,
    onEditClick: () -> Unit,
    onPastEventsClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

    // Dohvati podatke organizatora
    LaunchedEffect(organizerId) {
        val organizer = organizerService.getOrganizerById(organizerId)
        if (organizer != null) {
            name = organizer.name
            surname = organizer.surname
            email = organizer.email
            contact = organizer.contact
            birthDate = organizer.birthDate?.let {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("d. MMMM yyyy", Locale.getDefault())
                val date = inputFormat.parse(it)
                outputFormat.format(date)
            } ?: "Nedostupno"
        } else {
            Log.d("OrganizerProfile", "No data found for organizerId: $organizerId")
        }
    }

    // Provjeri je li prijavljeni organizator isti kao onaj koji se prikazuje
    val isLoggedInOrganizer = Auth.loggedInUserData?.organizer?.id == organizerId
    Log.d("OrganizerProfile", "Logged-in user ID: ${Auth.loggedInUserData?.id}, Organizer ID: $organizerId")
    Log.d("OrganizerProfile", "isLoggedInOrganizer: $isLoggedInOrganizer")

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Profile", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6200EE))

        Spacer(modifier = Modifier.height(16.dp))

        // Prikaz detalja organizatora
        ProfileDetailField("Name", "$name $surname")
        ProfileDetailField("Email", email)
        ProfileDetailField("Contact", contact)
        ProfileDetailField("Birth Date", birthDate)

        Spacer(modifier = Modifier.height(16.dp))

        // Gumb za uređivanje profila (ako je prijavljeni organizator)
        if (isLoggedInOrganizer) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { onEditClick() }) {
                    Text("Edit Profile")
                }
            }
        } else {
            Text("You do not have permission to edit this profile.", fontSize = 14.sp, color = Color.Gray)
        }

        // Gumb za prethodne događaje
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { onPastEventsClick() }) {
                Text("Prethodni događaji")
            }
        }
    }
}

@Composable
fun ProfileDetailField(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "$label:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        TextField(
            value = value,
            onValueChange = {},
            label = { Text(label) },
            enabled = false,
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
