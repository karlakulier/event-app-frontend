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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.eventapp.viewmodels.GetEventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onApplyFilter: (String, String, List<String>) -> Unit,
    viewModel: GetEventViewModel
) {
    if (isVisible) {
        var location by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }
        var date by remember { mutableStateOf("") }
        var apiFormattedDate by remember { mutableStateOf("") }
        val selectedGenres = remember { mutableStateListOf<String>() }
        val genres = listOf("Blues", "Jazz", "Pop", "Rock", "Opera", "Electronic", "Country", "Punk", "Rap", "Metal")
        val cities = listOf("Zagreb", "Split", "Rijeka", "Osijek", "Zadar", "Pula", "Slavonski Brod", "Karlovac", "Varaždin", "Šibenik", "Dubrovnik")
        var showDatePicker by remember { mutableStateOf(false) }

        LaunchedEffect(isVisible) {
            if (!isVisible) {
                location = ""
                date = ""
                selectedGenres.clear()
            }
        }

        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("FILTER BY", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Location") },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown",
                                    modifier = Modifier.clickable { expanded = true }
                                )
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            cities.forEach { city ->
                                DropdownMenuItem(
                                    text = { Text(city) },
                                    onClick = {
                                        location = city
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

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
                        readOnly = true,
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
                    onApplyFilter(location, apiFormattedDate, selectedGenres)
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
        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { displayDate, apiDate ->
                    date = displayDate
                    apiFormattedDate = apiDate
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}
