package com.example.eventapp.userInterface

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.PatternsCompat
import java.util.*
import java.util.regex.Pattern
import com.example.eventapp.utils.isPasswordValid
import com.example.eventapp.utils.isEmailValid
import com.example.ws.repository.UserRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun OrganizerRegistrationScreen(
    onBackClick: () -> Unit,
    onRegisterClick: () -> Unit,
    userRepository: UserRepository
) {
    var username by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var registrationSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var showErrors by remember { mutableStateOf(false) }


    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Welcome Organizer!", fontSize = 30.sp, modifier = Modifier.padding(bottom = 24.dp))
        Text(text = "Please fill in your information", fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

        TextFieldWithValidation(
            value = username,
            onValueChange = { if (it.length <= 40) username = it },
            label = "Username",
            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextFieldWithValidation(
            value = name,
            onValueChange = { if (it.length <= 30) name = it },
            label = "Name",
            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextFieldWithValidation(
            value = surname,
            onValueChange = { if (it.length <= 30) surname = it },
            label = "Surname",
            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            trailingIcon = {
                val icon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(imageVector = icon, contentDescription = "Toggle Password Visibility")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (showErrors && !isPasswordValid(password)) {
            Text("Password must contain at least 8 characters, 1 uppercase, and 1 number", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )
        if (showErrors && confirmPassword != password) {
            Text("Passwords do not match", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail address") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        if (showErrors && !isEmailValid(email)) {
            Text("Invalid email format", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = contact,
            onValueChange = { if (it.length <= 15 && it.all { char -> char.isDigit() }) contact = it },
            label = { Text("Contact") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = {
            DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    dateOfBirth = "$selectedYear-${(selectedMonth + 1).toString().padStart(2, '0')}-${selectedDay.toString().padStart(2, '0')}"
                },
                year,
                month,
                day
            ).show()
        }) {
            Text(text = if (dateOfBirth.isEmpty()) "Select Birth Date" else dateOfBirth)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                coroutineScope.launch {
                    try {
                        val formattedDate = dateOfBirth
                        val success = userRepository.registerOrganizer(
                            username, password, name, surname, email, contact, formattedDate)

                        isLoading = false
                        registrationSuccess = success

                        if (success) {
                            onRegisterClick()
                        } else {
                            errorMessage = "Registration failed. Please try again."
                        }
                    } catch (e: Exception) {
                        isLoading = false
                        errorMessage = "Error: ${e.localizedMessage}"
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) "Registering..." else "Register")
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onBackClick() },
            modifier = Modifier
                .width(150.dp)
                .height(48.dp)
                .border(1.dp, Color.Red, MaterialTheme.shapes.small),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text(text = "Back", color = Color.Red)
        }

    }
}
