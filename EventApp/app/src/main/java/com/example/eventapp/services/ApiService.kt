package com.example.eventapp.services

import com.example.eventapp.models.ApiResponse
import com.example.eventapp.models.MusicianRegistration
import com.example.eventapp.models.OrganizerRegistration
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/musicians/register")
    suspend fun registerMusician(@Body musician: MusicianRegistration): ApiResponse

    @POST("api/organizers/register")
    suspend fun registerOrganizer(@Body organizer: OrganizerRegistration): ApiResponse
}