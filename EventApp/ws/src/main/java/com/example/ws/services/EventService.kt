package com.example.ws.services


import com.example.core.network.models.EventDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventService {
    @POST("/api/Events/CreateEvent")
    fun createEvent(@Body eventDto: EventDto): Call<EventDto>

    @POST("api/Events/CancelEvent/{eventId}")
    fun cancelEvent(@Path("eventId") eventId: Int): Call<ResponseBody>

    @GET("api/Events/details/{eventId}")
    fun getEventDetails(@Path("eventId") eventId: Int): Call<EventDto>
}