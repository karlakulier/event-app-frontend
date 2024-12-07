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

@Composable
fun MusicianRegistrationScreen(
    onBackClick: () -> Unit,
    onRegisterClick: (Map<String, String>) -> Unit
) {
    val context = LocalContext.current

    var displayName by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var showErrors by remember { mutableStateOf(false) } // Kontrola grešaka

    // Kalendarski dijalog
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            dateOfBirth = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
        },
        year,
        month,
        day
    )



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Welcome Musician!", fontSize = 30.sp, modifier = Modifier.padding(bottom = 24.dp))
        Text(text = "Please fill in your information", fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Display Name
        TextFieldWithValidation(
            value = displayName,
            onValueChange = { if (it.length <= 30) displayName = it },
            label = "Display name",
            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Name
        TextFieldWithValidation(
            value = name,
            onValueChange = { if (it.length <= 30) name = it },
            label = "Name",
            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Surname
        TextFieldWithValidation(
            value = surname,
            onValueChange = { if (it.length <= 30) surname = it },
            label = "Surname",
            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password
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

        // Confirm Password
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

        // Email
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

        // Contact
        TextField(
            value = contact,
            onValueChange = { if (it.length <= 15 && it.all { char -> char.isDigit() }) contact = it },
            label = { Text("Contact") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Date of Birth/Formation Date
        TextField(
            value = dateOfBirth,
            onValueChange = { dateOfBirth = it },
            label = { Text("Date of birth/formation date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    datePickerDialog.show()
                }) {
                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Select Date")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Register Button
        Button(
            onClick = {
                showErrors = true
                if (displayName.isNotEmpty() && name.isNotEmpty() && surname.isNotEmpty()
                    && isPasswordValid(password) && confirmPassword == password
                    && isEmailValid(email) && contact.isNotEmpty() && dateOfBirth.isNotEmpty()
                ) {
                    onRegisterClick(
                        mapOf(
                            "displayName" to displayName,
                            "name" to name,
                            "surname" to surname,
                            "password" to password,
                            "email" to email,
                            "contact" to contact,
                            "dateOfBirth" to dateOfBirth
                        )
                    )
                } else {
                    Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Back Button
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

@Composable
fun TextFieldWithValidation(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = keyboardOptions,
        modifier = Modifier.fillMaxWidth()
    )
}

fun isPasswordValid(password: String): Boolean {
    val passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z]).{8,}$")
    return passwordPattern.matcher(password).matches()
}

fun isEmailValid(email: String): Boolean {
    return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
}
