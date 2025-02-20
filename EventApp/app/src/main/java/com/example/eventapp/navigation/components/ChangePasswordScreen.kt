package com.example.eventapp.navigation.components


import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.core.login.Auth
import com.example.ws.services.PasswordService
import kotlinx.coroutines.launch
import com.example.core.network.models.PasswordProfile
import com.example.eventapp.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    userId: Int,
    navController: NavController,
    passwordService: PasswordService
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val isOrganizer = Auth.loggedInUserData?.role == "Organizer"

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.app_logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(200.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        val userRole = Auth.loggedInUserData?.role
                        Log.d("Navigation", "User role: $userRole")
                        val destination = if (userRole == "Organizer") {
                            "organizerProfileViewOnly/${Auth.loggedInUserData?.organizer?.id}"
                        } else {
                            "artistProfileScreen"
                        }
                        navController.navigate(destination)
                    }) {
                        Icon(Icons.Default.Person, contentDescription = "User Icon", modifier = Modifier.padding(start = 16.dp)) }
                },
                actions = {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu Icon",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            if (isOrganizer) {
                OrganizerFooter(navController = navController)
            } else {
                ClassicFooter(navController = navController)
            }
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Change Password",
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

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }

            Button(
                onClick = {
                    if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                        errorMessage = "All fields are required!"
                        return@Button
                    }
                    if (newPassword != confirmPassword) {
                        errorMessage = "Passwords do not match!"
                        return@Button
                    }

                    val updatedProfile = PasswordProfile(
                        id = userId,
                        currentPassword = currentPassword,
                        newPassword = newPassword,
                        confirmPassword = confirmPassword,
                    )

                    coroutineScope.launch {
                        val result = passwordService.updatePasswordProfile(
                            userId, updatedProfile
                        )
                        if (result.isSuccess) {
                            snackbarHostState.showSnackbar("Password updated successfully!")
                            navController.popBackStack()
                        } else {
                            errorMessage = result.message
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Save", color = Color.White)
            }
        }
    }
}

@Composable
fun PasswordEditFields(label: String, value: String, onValueChange: (String) -> Unit) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "$label:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    val icon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    Icon(imageVector = icon, contentDescription = null)
                }
            },
            value = value,
            onValueChange = { onValueChange(it) },
            label = { Text("") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

