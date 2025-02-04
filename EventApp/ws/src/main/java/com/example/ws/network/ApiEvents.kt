package com.example.ws.network

import com.example.ws.models.GetEventData
import retrofit2.Call
import retrofit2.http.GET

interface ApiEvents {
    @GET("api/Events/ActiveEvents")
    fun getActiveEvents(): Call<List<GetEventData>>
}