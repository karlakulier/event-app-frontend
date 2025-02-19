package com.example.eventapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.network.ResponseListener
import com.example.core.network.models.ErrorResponseBody
import androidx.lifecycle.viewModelScope
import com.example.core.network.models.OrganizerProfile
import com.example.core.network.models.Review
import com.example.ws.models.EventFilterData
import com.example.ws.models.GetEventData
import com.example.ws.network.RetrofitClient
import com.example.ws.network.RetrofitClient.apiEvents
import com.example.ws.repository.EventRepository
import com.example.ws.request_handlers.GetEventRequestHandler
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class GetEventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _events = MutableLiveData<List<GetEventData>>()
    val events: LiveData<List<GetEventData>> = _events
    private val _eventDetails = MutableLiveData<GetEventData>()
    val eventDetails: LiveData<GetEventData> = _eventDetails
    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews
    private val _organizer = MutableLiveData<OrganizerProfile?>()
    val organizer: LiveData<OrganizerProfile?> = _organizer
    private val _pastEvents = MutableLiveData<List<GetEventData>>()
    val pastEvents: LiveData<List<GetEventData>> = _pastEvents



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
        eventRequestHandler.fetchPastEventsForMusician(
            musicianId,
            object : ResponseListener<List<GetEventData>> {
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

    fun fetchPastEventsByOrganizer(organizerId: Int) {
        val handler = GetEventRequestHandler()

        handler.fetchPastEventsByOrganizer(
            organizerId,
            object : ResponseListener<List<GetEventData>> {
                override fun onSuccessfulResponse(response: List<GetEventData>) {
                    _events.postValue(response)
                }

                override fun onErrorResponse(errorResponse: ErrorResponseBody) {
                    Log.e("GetEventViewModel", "Error fetching events: ${errorResponse.message}")
                }

                override fun onNetworkFailure(t: Throwable) {
                    Log.e("GetEventViewModel", "Network failure: ${t.message}")
                }
            })
    }

    fun fetchEventDetails(eventId: Int) {
        val call = RetrofitClient.apiEvents.getEventDetails(eventId)

        call.enqueue(object : Callback<GetEventData> {
            override fun onResponse(
                call: Call<GetEventData>,
                response: Response<GetEventData>
            ) {
                if (response.isSuccessful) {
                    _eventDetails.postValue(response.body()) // Post the single event to _eventDetails
                } else {
                    Log.e("GetEventViewModel", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetEventData>, t: Throwable) {
                Log.e("GetEventViewModel", "Failed to fetch event details", t)
            }
        })
    }

    fun fetchSortedEvents(sortBy: String) {
        viewModelScope.launch {
            try {
                Log.d("GetEventViewModel", "Fetching sorted events with sortBy: $sortBy")
                val response = eventRepository.getSortedEvents(sortBy)

                if (response.isSuccessful) {
                    Log.d("GetEventViewModel", "Fetched events: ${response.body()?.size ?: 0}")
                    _events.postValue(response.body() ?: emptyList())
                } else {
                    Log.e(
                        "GetEventViewModel",
                        "Error fetching sorted events: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("GetEventViewModel", "Exception fetching events", e)
            }
        }
    }
    fun fetchEventReviews(eventId: Int) {
        viewModelScope.launch {
            try {
                // getEventReviews sada vraća List<Review>
                val reviewsList = eventRepository.getEventReviews(eventId)
                _reviews.postValue(reviewsList)
            } catch (e: Exception) {
                _reviews.postValue(emptyList())
            }
        }
    }

}