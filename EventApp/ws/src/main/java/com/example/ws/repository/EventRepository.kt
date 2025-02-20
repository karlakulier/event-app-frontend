package com.example.ws.repository

import android.util.Log
import com.example.core.network.models.OrganizerProfile
import com.example.core.network.models.Review
import com.example.ws.models.EventDetails
import com.example.ws.models.GetEventData
import com.example.ws.network.ApiClient
import com.example.ws.network.ApiEvents
import com.example.ws.network.OrganizerAPI
import com.example.ws.network.RetrofitClient
import com.example.ws.network.RetrofitClient.apiEvents
import com.example.ws.network.RetrofitClient.apiService
import retrofit2.Response

class EventRepository {
    private val api = ApiClient.api
    private val apievents = RetrofitClient.apiEvents

    suspend fun getSortedEvents(sortBy: String): Response<List<GetEventData>> {
        return try {
            Log.d("EventRepository", "Sending GET request for events sorted by: $sortBy")
            val response = api.getEvents(sortBy)

            if (response.isSuccessful) {
                Log.d("EventRepository", "Received events: ${response.body()?.size ?: 0}")
            } else {
                Log.e("EventRepository", "API error: ${response.errorBody()?.string()}")
            }

            response
        } catch (e: Exception) {
            Log.e("EventRepository", "Exception in getSortedEvents()", e)
            Response.error(500, okhttp3.ResponseBody.create(null, "Internal error"))
        }
    }
    suspend fun getEventReviews(eventId: Int): List<Review> {
        return try {
            RetrofitClient.reviewApi.getReviewsByEvent(eventId)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAllPastEvents(): List<GetEventData> {
        return try {
            val response = apievents.getAllPastEvents()
            if (response.isSuccessful) {
                Log.d("EventRepository", "Fetched past events: ${response.body()?.size ?: 0}")
                response.body() ?: emptyList()
            } else {
                Log.e("EventRepository", "Error fetching past events: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("EventRepository", "Exception in getAllPastEvents(): ${e.message}")
            emptyList()
        }
    }

    suspend fun getActiveEventsByOrganizer(organizerId : Int) : List<GetEventData> {
            return  apievents.getActiveEventsByOrganizer(organizerId)
    }

    suspend fun searchEvents(query: String): Response<List<GetEventData>> {
        return try {
            Log.d("EventRepository", "Searching events with keyword: $query")
            val response = apiEvents.searchEvents(query)

            if (response.isSuccessful) {
                Log.d("EventRepository", "Received events: ${response.body()?.size ?: 0}")
            } else {
                Log.e("EventRepository", "API error: ${response.errorBody()?.string()}")
            }

            response
        } catch (e: Exception) {
            Log.e("EventRepository", "Exception in searchEvents()", e)
            Response.error(500, okhttp3.ResponseBody.create(null, "Internal error"))
        }
    }


    suspend fun getEventDetailsGlobal(eventId: Int): EventDetails? {
        return try {
            val response = apievents.getEventDetailsGlobal(eventId)
            Log.d("API_CALL", "Response: $response")
            response // Retrofit automatski obrađuje odgovor
        } catch (e: Exception) {
            Log.e("API_EXCEPTION", "Exception occurred", e)
            null
        }
    }
}