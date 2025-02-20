package com.example.eventapp.navigation.components

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationRequest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.core.login.Auth
import com.example.eventapp.R
import com.example.core.network.models.EventDto

import com.example.ws.network.EventRetrofitInstance
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

@Composable
fun EventInputScreen(navController: NavController, modifier: Modifier = Modifier) {
    var eventName by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var musicGenre by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var isDatePickerOpen by remember { mutableStateOf(false) }
    var isStartTimePickerOpen by remember { mutableStateOf(false) }
    var isEndTimePickerOpen by remember { mutableStateOf(false) }
    var relevanceScore by remember { mutableStateOf(0) }
    val loggedInUserRole = remember { Auth.loggedInUserData?.role }

    val finished = false
    val organizerId = Auth.loggedInUserData?.organizer?.id ?: -1


    val dateTimeFormatted = try {
        if (date.isNotEmpty() && startTime.isNotEmpty()) {
            val inputFormat = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault())
            val parsedDate = inputFormat.parse("$date $startTime")

            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getTimeZone("UTC")
            outputFormat.format(parsedDate)
        } else {
            ""
        }
    } catch (e: Exception) {
        Log.e("DateTimeFormat", "Greška pri konverziji datuma: ${e.localizedMessage}")
        ""
    }



    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Header(navController)
        TextField(
            value = eventName,
            onValueChange = { eventName = it },
            label = { Text("Name of the event") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        TextField(
            value = capacity,
            onValueChange = { capacity = it },
            label = { Text("Capacity") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Unos za opis
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description and announcement of the event") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        LocationPickerField(location) { selectedLocation -> location = selectedLocation }

        Spacer(modifier = Modifier.height(16.dp))


        TextField(
            value = musicGenre,
            onValueChange = { musicGenre = it },
            label = { Text("Music Genre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        TextField(
            value = relevanceScore.toString(),
            onValueChange = {
                relevanceScore = it.toIntOrNull() ?: 0
            },
            label = { Text("Relevance Score") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = date,
            onValueChange = { },
            readOnly = true,
            label = { Text("Date (MM/DD/YYYY)") },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Pick a date",
                    modifier = Modifier.clickable {
                        isDatePickerOpen = true
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (isDatePickerOpen) {
            AlertDialog(
                onDismissRequest = { isDatePickerOpen = false },
                confirmButton = {
                    DatePickerDialog(
                        onDateSelected = { selectedDate ->
                            date = selectedDate
                            isDatePickerOpen = false
                        },
                        onDismissRequest = { isDatePickerOpen = false }
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = { isStartTimePickerOpen = true }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.time),
                    contentDescription = "Pick start time",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = if (startTime.isEmpty()) "Pick start time" else startTime)
            }

            OutlinedButton(
                onClick = { isEndTimePickerOpen = true }
            ) { Icon(
                painter = painterResource(id = R.drawable.time),
                contentDescription = "Pick end time",
                modifier = Modifier.size(20.dp)
            )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = if (endTime.isEmpty()) "Pick end time" else endTime)
            }
        }

        if (isStartTimePickerOpen) {
            AlertDialog(
                onDismissRequest = { isStartTimePickerOpen = false },
                confirmButton = {
                    TimePickerDialog(onTimeSelected = {
                        startTime = it
                        isStartTimePickerOpen = false
                    }, onDismissRequest = { isStartTimePickerOpen = false })
                }
            )
        }

        if (isEndTimePickerOpen) {
            AlertDialog(
                onDismissRequest = { isEndTimePickerOpen = false },
                confirmButton = {
                    TimePickerDialog(onTimeSelected = {
                        endTime = it
                        isEndTimePickerOpen = false
                    }, onDismissRequest = { isEndTimePickerOpen = false })
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    if (date.isEmpty() || startTime.isEmpty() || location.isEmpty()) {
                        return@Button
                    }

                    val eventDto = EventDto(
                        id = null,
                        dateTime = dateTimeFormatted,
                        location = location,
                        description = description,
                        finished = finished,
                        organizerId = organizerId,
                        musicGenre = musicGenre,
                        relevanceScore = relevanceScore.toString()
                    )

                    EventRetrofitInstance.apievent.createEvent(eventDto).enqueue(object : Callback<EventDto> {
                        override fun onResponse(call: Call<EventDto>, response: Response<EventDto>) {
                            if (response.isSuccessful) {
                                Log.d("EventCreate", "Događaj je uspješno kreiran: ${response.body()}")
                                navController.navigate("home")
                            } else {
                                Log.e("EventCreate", "Neuspješno kreiranje događaja. Kod: ${response.code()} - ${response.errorBody()?.string()}")
                            }
                        }
                        override fun onFailure(call: Call<EventDto>, t: Throwable) {
                            Log.e("EventCreate", "Greška: ${t.localizedMessage}")
                        }
                    })
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text("Save changes")
            }
        }


        Spacer(modifier = Modifier.height(16.dp))


            if (loggedInUserRole == "Organizer") {
                OrganizerFooter(navController = navController)
            } else {
                ClassicFooter(navController = navController)
            }
        }
    }



@Composable
fun DatePickerDialog(onDateSelected: (String) -> Unit, onDismissRequest: () -> Unit) {
    val calendar = Calendar.getInstance()
    val currentDate = Calendar.getInstance()
    val currentDateMillis = currentDate.timeInMillis

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismissRequest) {
        val datePickerDialog = DatePickerDialog(
            LocalContext.current,
            { _, year, month, dayOfMonth ->
                onDateSelected("${month + 1}/$dayOfMonth/$year")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )


        datePickerDialog.datePicker.minDate = currentDateMillis

        datePickerDialog.show()
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
@Composable
fun LocationPickerField(location: String, onLocationSelected: (String) -> Unit) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var query by remember { mutableStateOf(location) }
    var isLoading by remember { mutableStateOf(false) }

    Column {
        TextField(
            value = query,
            onValueChange = {
                query = it
                onLocationSelected(it)
            },
            label = { Text("Enter location") },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.location_icon),
                    contentDescription = "Use current location",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            isLoading = true
                            getCurrentLocation(context, fusedLocationClient) { address ->
                                query = address
                                onLocationSelected(address)
                                isLoading = false
                            }
                        }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(8.dp))
        }
    }
}



private fun getCurrentLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onAddressReceived: (String) -> Unit
) {

    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        onAddressReceived("Nema dozvole za lokaciju")
        return
    }

    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { location ->
            if (location != null) {
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                    if (!addresses.isNullOrEmpty()) {
                        onAddressReceived(addresses[0].getAddressLine(0))
                    } else {
                        onAddressReceived("Lokacija nije pronađena")
                    }
                } catch (e: Exception) {
                    onAddressReceived("Greška pri dohvaćanju adrese: ${e.localizedMessage}")
                }
            } else {
                onAddressReceived("Neuspješno dohvaćanje lokacije")
            }
        }
        .addOnFailureListener { exception ->
            onAddressReceived("Greška: ${exception.localizedMessage}")
        }
}
@Composable
fun RequestLocationPermissions(
    context: Context,
    scaffoldState: SnackbarHostState
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                CoroutineScope(Dispatchers.Main).launch {
                    scaffoldState.showSnackbar("Dozvola za lokaciju nije odobrena!", duration = SnackbarDuration.Short)
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}


