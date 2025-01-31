package com.example.eventapp

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eventapp.models.EventDto
import com.example.eventapp.services.EventRetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.platform.LocalContext
import okhttp3.ResponseBody

@Composable
fun CancelEventScreen(eventId: Int) {
    val context = LocalContext.current
    val event = remember { mutableStateOf<EventDto?>(null) }
    val isOrganizer = remember { mutableStateOf(false) }
    val cancelMessage = remember { mutableStateOf("") }

    // Dohvati detalje o događaju
    EventRetrofitInstance.apievent.getEventDetails(eventId)
        .enqueue(object : Callback<EventDto> {
            override fun onResponse(call: Call<EventDto>, response: Response<EventDto>) {
                if (response.isSuccessful) {
                    event.value = response.body()
                    val currentUserId = getCurrentUserId()
                    isOrganizer.value = event.value?.organizerId == currentUserId
                }
            }

            override fun onFailure(call: Call<EventDto>, t: Throwable) {
                cancelMessage.value = "Failed to load event details"
            }
        })


    cancelMessage.value?.let {
        Text(text = it)
    }


    event.value?.let {
        CancelEventView(event = it, isOrganizer = isOrganizer.value, onCancel = { cancelEvent(it.id!!, context, cancelMessage) })
    }
}

@Composable
fun CancelEventView(event: EventDto, isOrganizer: Boolean, onCancel: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Location: ${event.location}", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Date/Time: ${event.dateTime}", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Description: ${event.description}", style = MaterialTheme.typography.bodySmall)


        if (isOrganizer) {
            Button(
                onClick = { onCancel() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cancel Event")
            }
        }
    }
}

fun cancelEvent(eventId: Int, context: android.content.Context, cancelMessage: MutableState<String>) {
    EventRetrofitInstance.apievent.cancelEvent(eventId)
        .enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    cancelMessage.value = "Event cancelled successfully"
                    Toast.makeText(context, cancelMessage.value, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                cancelMessage.value = "Failed to cancel event"
                Toast.makeText(context, cancelMessage.value, Toast.LENGTH_SHORT).show()
            }
        })
}

fun getCurrentUserId(): Int {
    // ovo je samo primjer
    return 1  // Pretpostavljamo da je ovo ID za trenutno prijavljenog korisnika
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CancelEventScreen(eventId = 1)
}
