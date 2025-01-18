package com.example.eventapp.navigation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FilterDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onApplyFilter: (String, String, List<String>) -> Unit
) {
    if (isVisible) {
        var location by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }
        val selectedGenres = remember { mutableStateListOf<String>() }
        val genres = listOf("Blues", "Jazz", "Pop", "Rock", "Opera", "Electronic", "Country", "Punk", "Rap", "Metal")
        var showDatePicker by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("FILTER BY", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Release date input with DatePicker
                    OutlinedTextField(
                        value = date,
                        onValueChange = {},
                        label = { Text("Date") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = "Select date",
                                modifier = Modifier.clickable { showDatePicker = true }
                            )
                        },
                        readOnly = true, // Sprečava direktno uređivanje
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Genre", fontWeight = FontWeight.Bold)
                    LazyColumn {
                        items(genres) { genre ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = selectedGenres.contains(genre),
                                    onCheckedChange = { isChecked ->
                                        if (isChecked) {
                                            selectedGenres.add(genre)
                                        } else {
                                            selectedGenres.remove(genre)
                                        }
                                    }
                                )
                                Text(genre)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onApplyFilter(location, date, selectedGenres)
                    onDismiss()
                }) {
                    Text("Filter")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        )
        // DatePicker Modal
        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { millis ->
                    date = millis?.let { convertMillisToDate(it) } ?: ""
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}