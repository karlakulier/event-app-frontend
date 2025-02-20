package com.example.eventapp.navigation.components

import androidx.compose.ui.platform.LocalContext
import com.air.eventapplication.ApplicationModul
import com.air.eventapplication.models.ApplicationGet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ApplicationCard(application: ApplicationGet) {
    var showDialog by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(application.status) } // Lokalno stanje za status
    var context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) { // Lijeva strana (ime i status)
                Text(
                    text = "${application.musicianName} ${application.musicianSurname}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = status,
                    fontSize = 14.sp,
                )
            }

            // Desna strana (gumb)
            Button(
                onClick = { showDialog = true }
            ) {
                Text("Change Status", color = Color.White)
            }
        }
    }

    // Dijalog za odabir statusa
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Change Status") },
            text = {
                Column {
                    listOf("Accept", "Decline").forEach { newStatus ->
                        Button(
                            onClick = {
                                if(newStatus=="Accept"){
                                    selectedStatus = "Accepted"
                                }
                                if(newStatus=="Decline"){
                                    selectedStatus = "Declined"
                                }
                                showDialog = false
                                showConfirmation = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(newStatus)
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }

    // Potvrda odabira
    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            title = { Text("Confirm Change") },
            text = { Text("Are you sure you want to set status to $selectedStatus?") },
            confirmButton = {
                Button(
                    onClick = {
                        status = selectedStatus // Ažuriraj lokalni status
                        ApplicationModul.UpdateApplicationStatus(context,application.id,status)
                        showConfirmation = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmation = false }) {
                    Text("No")
                }
            }
        )
    }
}


@Composable
fun ApplicationsForEvent(navController: NavController,eventId: Int) {
    var applications by remember { mutableStateOf<List<ApplicationGet>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var context = LocalContext.current

    LaunchedEffect(Unit) {
        applications = ApplicationModul.GetApplicationsByEvent(context,eventId)
        isLoading = false
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Header(navController)
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                Text(
                    text = "Loading...",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            } else if (applications.isEmpty()) {
                Text(
                    text = "No applications for event.",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(applications) { application ->
                        // Postavljanje clickable direktno na ChatCard
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                //navController.navigate("messages/${application.id}") // Pravilna destinacija
                            }) {
                            ApplicationCard(application = application,)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
