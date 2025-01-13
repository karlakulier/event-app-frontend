package com.example.eventapp.navigation.components

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    onSaveChanges: () -> Unit
) {
    var name by remember { mutableStateOf("Ime") }
    var surname by remember { mutableStateOf("Prezime") }
    var email by remember { mutableStateOf("email@gmail.com") }
    var contact by remember { mutableStateOf("+385123456789") }
    var dateOfBirth by remember { mutableStateOf("11/11/1991") }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        navController.context,
        { _, year, month, dayOfMonth ->
            dateOfBirth = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Surname") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail address") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = contact,
                onValueChange = { contact = it },
                label = { Text("Contact") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = {},
                label = { Text("Date of Birth") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            //
            Button(
                onClick = { navController.navigate("changePasswordScreen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Change Password")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { navController.popBackStack() }) {
                    Text("Cancel")
                }
                Button(onClick = {
                    onSaveChanges()
                    navController.popBackStack()
                }) {
                    Text("Save Changes")
                }
            }
        }
    }
}
