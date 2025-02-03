package com.example.eventapp.navigation.components

import java.text.SimpleDateFormat
import java.util.*

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventapp.services.OrganizerRetrofit
import com.example.eventapp.services.OrganizerService

@Composable
fun OrganizerProfileViewOnly(
    organizerId: Int,
    organizerService: OrganizerService,
    onEditClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

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
            // Logiranje ako podaci nisu dostupni
            Log.d("OrganizerProfile", "No data found for organizerId: $organizerId")
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Profile", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6200EE))

        Spacer(modifier = Modifier.height(16.dp))

        // Prikaz podataka koristeći onemogućene TextField-e
        ProfileDetailField("Name", "$name $surname")
        ProfileDetailField("Email", email)
        ProfileDetailField("Contact", contact)
        ProfileDetailField("Birth Date", birthDate)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onEditClick() }) {
            Text("Edit Profile")
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
            enabled = false, // Onemogućuje unos
            readOnly = true, // Samo za prikaz
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun PreviewOrganizerProfileViewOnly() {
    val organizerService = OrganizerService(OrganizerRetrofit.api)
    val organizerId = 1
    OrganizerProfileViewOnly(organizerId = organizerId, organizerService = organizerService, onEditClick = {})
}
