package com.example.eventapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eventapp.R
import com.example.eventapp.navigation.components.Header
import com.example.eventapp.navigation.components.ClassicFooter

import java.util.Calendar

@Composable
fun EventInputScreen(navController: NavController, modifier: Modifier = Modifier) {
    var eventName by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var spaceDetails by remember { mutableStateOf("") }
    var isDatePickerOpen by remember { mutableStateOf(false) }
    var isStartTimePickerOpen by remember { mutableStateOf(false) }
    var isEndTimePickerOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header with Logo, Profile, and Menu
        Header()

        // Input Fields
        TextField(
            value = eventName,
            onValueChange = { eventName = it },
            label = { Text("Name of the event") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = capacity,
            onValueChange = { capacity = it },
            label = { Text("Capacity") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description and announcement of the event") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for Event Type
        DropdownMenu(eventType, onEventTypeSelected = { eventType = it })

        Spacer(modifier = Modifier.height(8.dp))

        // Date Picker
        TextField(
            value = date,
            onValueChange = { },
            readOnly = true,
            label = { Text("Date (MM/DD/YYYY)") },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.calendar), // Koristi calendar.png
                    contentDescription = "Pick a date",
                    modifier = Modifier.clickable { isDatePickerOpen = true }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (isDatePickerOpen) {
            DatePickerDialog(onDateSelected = {
                date = it
                isDatePickerOpen = false
            }, onDismissRequest = { isDatePickerOpen = false })
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Time Pickers
        Row {
            Text(
                text = if (startTime.isEmpty()) "Pick start time" else startTime,
                modifier = Modifier
                    .weight(1f)
                    .clickable { isStartTimePickerOpen = true }
                    .padding(end = 8.dp)
            )
            if (isStartTimePickerOpen) {
                TimePickerDialog(onTimeSelected = {
                    startTime = it
                    isStartTimePickerOpen = false
                }, onDismissRequest = { isStartTimePickerOpen = false })
            }

            Text(
                text = if (endTime.isEmpty()) "Pick end time" else endTime,
                modifier = Modifier
                    .weight(1f)
                    .clickable { isEndTimePickerOpen = true }
                    .padding(start = 8.dp)
            )
            if (isEndTimePickerOpen) {
                TimePickerDialog(onTimeSelected = {
                    endTime = it
                    isEndTimePickerOpen = false
                }, onDismissRequest = { isEndTimePickerOpen = false })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = spaceDetails,
            onValueChange = { spaceDetails = it },
            label = { Text("Description of space and specifics") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // File Import Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text("Import your files\nDrag or click to upload", textAlign = TextAlign.Center, fontSize = 14.sp)
        }

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { navController.popBackStack() }) {
                Text("Cancel")
            }
            Button(onClick = { /* Save logic */ }) {
                Text("Save changes")
            }
        }

        // Footer
        ClassicFooter(navController)
    }
}

@Composable
fun DropdownMenu(selectedType: String, onEventTypeSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val eventTypes = listOf("Concert", "Workshop", "Meeting", "Exhibition")

    Box {
        TextField(
            value = selectedType,
            onValueChange = { },
            readOnly = true,
            label = { Text("Event Type") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Dropdown Menu",
                    modifier = Modifier.clickable { expanded = true }
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            eventTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        onEventTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DatePickerDialog(onDateSelected: (String) -> Unit, onDismissRequest: () -> Unit) {
    val calendar = Calendar.getInstance()
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismissRequest) {
        DatePickerDialog(
            LocalContext.current,
            { _, year, month, dayOfMonth ->
                onDateSelected("${month + 1}/$dayOfMonth/$year")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}

@Composable
fun TimePickerDialog(onTimeSelected: (String) -> Unit, onDismissRequest: () -> Unit) {
    val calendar = Calendar.getInstance()
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismissRequest) {
        TimePickerDialog(
            LocalContext.current,
            { _, hour, minute ->
                onTimeSelected(String.format("%02d:%02d", hour, minute))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }
}
