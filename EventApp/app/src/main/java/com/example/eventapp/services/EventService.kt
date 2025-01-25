package com.example.eventapp.services


import com.example.eventapp.models.EventDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface EventService {
    @POST("events/CreateEvent")
    fun createEvent(@Body eventDto: EventDto): Call<EventDto>
}