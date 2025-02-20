package com.example.ws.network

import com.example.core.network.models.EventDto
import com.example.ws.models.EventDetails
import com.example.ws.models.EventFilterData
import com.example.ws.models.GetEventData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.POST

interface ApiEvents {
    @GET("api/Events/ActiveEvents")
    fun getActiveEvents(): Call<List<GetEventData>>

    @GET("api/Events/activeEvnetsByOrganizer")
    suspend fun getActiveEventsByOrganizer(@Query("organizerId") organizerId: Int): List<GetEventData>

    @POST("api/Events/filter")
    fun getFilteredEvents(@Body filter: EventFilterData): Call<List<GetEventData>>

    @GET("api/Events/PastEventByMusicanId/{musicianId}")
    fun getPastEventsForMusician(@Path("musicianId") musicianId: Int): Call<List<GetEventData>>

    @GET("api/Events/PastEventByOrganizerId/{organizerId}")
    fun getPastEventsByOrganizer(
        @Path("organizerId") organizerId: Int,
        @Query("finished") finished: Boolean = true
    ): Call<List<GetEventData>>

    @GET("api/Events/details/{eventId}")
    fun getEventDetails(@Path("eventId") eventId: Int): Call<GetEventData>

    @GET("api/Events/details/{eventId}")
    suspend fun getEventDetailsGlobal(@Path("eventId") eventId: Int): EventDetails

    @GET("api/Events/past-events")
    suspend fun getAllPastEvents(): Response<List<GetEventData>>

    @GET("api/Events/search")
    suspend fun searchEvents(@Query("keyword") keyword: String): Response<List<GetEventData>>
}