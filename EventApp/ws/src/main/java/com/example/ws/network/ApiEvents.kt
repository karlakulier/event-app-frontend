package com.example.ws.network

import com.example.ws.models.GetEventData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEvents {
    @GET("api/Events/ActiveEvents")
    fun getActiveEvents(): Call<List<GetEventData>>
    @GET("api/Events/PastEventByMusicanId/{musicianId}")
    fun getPastEventsForMusician(@Path("musicianId") musicianId: Int): Call<List<GetEventData>>
}