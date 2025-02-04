package com.example.ws.network

import com.example.ws.models.EventFilterData
import com.example.ws.models.GetEventData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiEvents {
    @GET("api/Events/ActiveEvents")
    fun getActiveEvents(): Call<List<GetEventData>>

    @POST("api/Events/filter")
    fun getFilteredEvents(@Body filter: EventFilterData): Call<List<GetEventData>>
}