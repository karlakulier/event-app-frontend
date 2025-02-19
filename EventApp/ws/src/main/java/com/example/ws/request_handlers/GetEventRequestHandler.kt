package com.example.ws.request_handlers

import android.util.Log
import com.example.core.network.ResponseListener
import com.example.core.network.models.ErrorResponseBody
import com.example.ws.models.GetEventData
import com.example.ws.network.RetrofitClient
import com.example.ws.network.RetrofitClient.apiEvents
import com.example.ws.network.RetrofitClient.apiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetEventRequestHandler {
    fun fetchActiveEvents(responseListener: ResponseListener<List<GetEventData>>) {
        val call = apiEvents.getActiveEvents()

        call.enqueue(object : Callback<List<GetEventData>> {
            override fun onResponse(
                call: Call<List<GetEventData>>,
                response: Response<List<GetEventData>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    responseListener.onSuccessfulResponse(response.body()!!)
                } else {
                    Log.e("EventRequestHandler", "Failed to fetch events")
                    responseListener.onErrorResponse(
                        ErrorResponseBody(400, "No events found")
                    )
                }
            }

            override fun onFailure(call: Call<List<GetEventData>>, t: Throwable) {
                responseListener.onNetworkFailure(t)
            }
        })
    }
    fun fetchPastEventsForMusician(musicianId: Int, responseListener: ResponseListener<List<GetEventData>>) {
        val call = apiEvents.getPastEventsForMusician(musicianId)  // Prosljeđujemo ID glazbenika

        call.enqueue(object : Callback<List<GetEventData>> {
            override fun onResponse(
                call: Call<List<GetEventData>>,
                response: Response<List<GetEventData>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    responseListener.onSuccessfulResponse(response.body()!!)
                } else {
                    Log.e("GetEventRequestHandler", "Failed to fetch past events")
                    responseListener.onErrorResponse(
                        ErrorResponseBody(response.code(), response.message())
                    )
                }
            }

            override fun onFailure(call: Call<List<GetEventData>>, t: Throwable) {
                responseListener.onNetworkFailure(t)
            }
        })
    }
    fun fetchPastEventsByOrganizer(organizerId: Int, responseListener: ResponseListener<List<GetEventData>>) {
        val call = RetrofitClient.apiEvents.getPastEventsByOrganizer(organizerId, finished = true)

        call.enqueue(object : Callback<List<GetEventData>> {
            override fun onResponse(
                call: Call<List<GetEventData>>,
                response: Response<List<GetEventData>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    responseListener.onSuccessfulResponse(response.body()!!)
                } else {
                    Log.e("GetEventRequestHandler", "Failed to fetch past events for organizer")
                    responseListener.onErrorResponse(
                        ErrorResponseBody(response.code(), response.message())
                    )
                }
            }

            override fun onFailure(call: Call<List<GetEventData>>, t: Throwable) {
                responseListener.onNetworkFailure(t)
            }
        })
    }
}