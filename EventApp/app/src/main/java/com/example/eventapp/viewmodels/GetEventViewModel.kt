package com.example.eventapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.network.ResponseListener
import com.example.core.network.models.ErrorResponseBody
import androidx.lifecycle.viewModelScope
import com.example.ws.models.EventFilterData
import com.example.ws.models.GetEventData
import com.example.ws.network.RetrofitClient.apiEvents
import com.example.ws.request_handlers.GetEventRequestHandler
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class GetEventViewModel : ViewModel() {
    private val _events = MutableLiveData<List<GetEventData>>()
    val events: LiveData<List<GetEventData>> = _events

    private val eventRequestHandler = GetEventRequestHandler()

    fun fetchEvents() {
        val call = apiEvents.getActiveEvents()

        call.enqueue(object : Callback<List<GetEventData>> {
            override fun onResponse(
                call: Call<List<GetEventData>>,
                response: Response<List<GetEventData>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _events.postValue(response.body()!!)
                } else {
                    Log.e("GetEventViewModel", "Error: ${response.code()} - ${response.message()}")
                    _events.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<GetEventData>>, t: Throwable) {
                Log.e("GetEventViewModel", "Failed to fetch events", t)
                _events.postValue(emptyList())
            }
        })
    }
    fun fetchFilteredEvents(location: String, date: String, genres: List<String>) {
        viewModelScope.launch {
            try {
                val response = apiEvents.getActiveEvents().awaitResponse()
                if (response.isSuccessful) {
                    val filteredEvents = response.body()?.filter {
                        (location.isEmpty() || it.location.contains(location, ignoreCase = true)) &&
                                (date.isEmpty() || it.dateTime.startsWith(date)) &&
                                (genres.isEmpty() || genres.contains(it.musicGenre))
                    } ?: emptyList()
                    _events.postValue(filteredEvents)
                } else {
                    _events.postValue(emptyList())
                }
            } catch (e: Exception) {
                _events.postValue(emptyList())
            }
        }
    }


    fun fetchPastEventsForMusician(musicianId: Int) {
        eventRequestHandler.fetchPastEventsForMusician(musicianId, object : ResponseListener<List<GetEventData>> {
            override fun onSuccessfulResponse(response: List<GetEventData>) {
                _events.postValue(response)
            }

            override fun onErrorResponse(error: ErrorResponseBody) {
                Log.e("GetEventViewModel", "Error: ${error.statusCode} - ${error.message}")
                _events.postValue(emptyList())
            }

            override fun onNetworkFailure(t: Throwable) {
                Log.e("GetEventViewModel", "Failed to fetch past events", t)
                _events.postValue(emptyList())
            }
        })
    }

    fun fetchPastEventsForOrganizer(organizerId: Int) {
        eventRequestHandler.fetchPastEventsForOrganizer(organizerId, object : ResponseListener<List<GetEventData>> {
            override fun onSuccessfulResponse(response: List<GetEventData>) {
                _events.value = response
            }

            override fun onErrorResponse(error: ErrorResponseBody) {
                Log.e("GetEventViewModel", "Error fetching past events: ${error.message}")
            }

            override fun onNetworkFailure(t: Throwable) {
                Log.e("GetEventViewModel", "Network failure: ${t.message}")
            }
        })
    }

}