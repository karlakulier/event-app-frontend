package com.example.eventapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eventapp.models.EventDto
import com.example.eventapp.navigation.components.ClassicFooter
import com.example.eventapp.navigation.components.Header
import com.example.eventapp.services.EventRetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EventInputScreen(navController: NavController, modifier: Modifier = Modifier) {
    var eventName by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var isDatePickerOpen by remember { mutableStateOf(false) }
    var isStartTimePickerOpen by remember { mutableStateOf(false) }
    var isEndTimePickerOpen by remember { mutableStateOf(false) }

    // Formatiranje dateTime za backend
    val dateTime = if (date.isNotEmpty() && startTime.isNotEmpty()) {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val formattedDate = dateFormat.parse(date)
        val formattedTime = timeFormat.parse(startTime)

        val calendar = Calendar.getInstance()
        calendar.time = formattedDate
        calendar.set(Calendar.HOUR_OF_DAY, formattedTime.hours)
        calendar.set(Calendar.MINUTE, formattedTime.minutes)

        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(calendar.time)
    } else {
        ""
    }

    // Simulacija finished i organizerId
    val finished = false
    val organizerId = 1

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
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

        // Date Picker s ikonom
        TextField(
            value = date,
            onValueChange = { },
            readOnly = true,
            label = { Text("Date (MM/DD/YYYY)") },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
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
            Icon(
                painter = painterResource(id = R.drawable.time),
                contentDescription = "Pick start time",
                modifier = Modifier.clickable { isStartTimePickerOpen = true }
            )
            if (isStartTimePickerOpen) {
                TimePickerDialog(onTimeSelected = {
                    startTime = it
                    isStartTimePickerOpen = false
                }, onDismissRequest = { isStartTimePickerOpen = false })
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (endTime.isEmpty()) "Pick end time" else endTime,
                modifier = Modifier
                    .weight(1f)
                    .clickable { isEndTimePickerOpen = true }
                    .padding(start = 8.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.time),
                contentDescription = "Pick end time",
                modifier = Modifier.clickable { isEndTimePickerOpen = true }
            )
            if (isEndTimePickerOpen) {
                TimePickerDialog(onTimeSelected = {
                    endTime = it
                    isEndTimePickerOpen = false
                }, onDismissRequest = { isEndTimePickerOpen = false })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { navController.popBackStack() }) {
                Text("Cancel")
            }
            Button(onClick = {
                if (date.isEmpty() || startTime.isEmpty() || location.isEmpty()) {
                    // Error handling
                    return@Button
                }

                val eventDto = EventDto(
                    id = null,
                    dateTime = dateTime,
                    location = location,
                    description = description,
                    finished = finished,
                    organizerId = organizerId
                )

                EventRetrofitInstance.apievent.createEvent(eventDto).enqueue(object : Callback<EventDto> {
                    override fun onResponse(call: Call<EventDto>, response: Response<EventDto>) {
                        if (response.isSuccessful) {
                            // Logiraj odgovor za debugiranje
                            Log.d("EventCreate", "Događaj je uspješno kreiran: ${response.body()}")
                        } else {
                            Log.e("EventCreate", "Neuspješno kreiranje događaja. Status kod: ${response.code()} | Poruka: ${response.errorBody()?.string()}")

                        }
                    }

                    override fun onFailure(call: Call<EventDto>, t: Throwable) {
                        // Obrada pogreške
                        Log.e("EventCreate", "Greška: ${t.localizedMessage}")
                    }
                })
            }) {
                Text("Save changes")
            }
        }

        ClassicFooter(navController)
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
