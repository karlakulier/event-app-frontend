package com.example.eventapp.navigation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ws.services.PasswordService
import kotlinx.coroutines.launch
import com.example.core.network.models.PasswordProfile
import kotlinx.coroutines.delay

@Composable
fun PasswordProfile(
    userId: Int,
    passwordService: PasswordService,
    navController: NavController
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }


    LaunchedEffect(userId) {
        val user = passwordService.getUserById(userId)
        if (user != null) {
            currentPassword = user.currentPassword
            newPassword = user.currentPassword
            confirmPassword = user.currentPassword
        }
    }

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Edit Password",
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 24.dp),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000000)
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordEditFields("Current Password", currentPassword) { currentPassword = it }
        PasswordEditFields("New Password", newPassword) { newPassword = it }
        PasswordEditFields("Confirm Password", confirmPassword) { confirmPassword = it }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    navController.navigate("passwordProfileViewOnly/$userId") {
                        popUpTo("passwordProfile/$userId") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {

                    val updatedProfile = PasswordProfile(
                        id = userId,
                        currentPassword = currentPassword,
                        newPassword = newPassword,
                        confirmPassword = confirmPassword,
                    )
                    coroutineScope.launch {

                        val result =
                            passwordService.updatePasswordProfile(userId, updatedProfile)
                        dialogMessage = "Lozinka je uspješno ažurirana!"
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
                    navController.navigate("passwordProfileViewOnly/$userId") {
                        popUpTo("passwordProfile/$userId") { inclusive = true }
                    }
                },
                title = { Text("Password Update") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            navController.navigate("passwordProfileViewOnly/$userId") {
                                popUpTo("passwordProfile/$userId") { inclusive = true }
                            }
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

