package com.example.ws.repository

import android.util.Log
import com.example.ws.models.GetEventData
import com.example.ws.network.ApiClient
import com.example.ws.network.UserApi
import retrofit2.Response

class EventRepository {
    private val api = ApiClient.api

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
}