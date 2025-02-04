package com.example.ws.network

import com.example.ws.models.RegisterBodyOrganizer
import com.example.ws.models.RegisterBodyMusician
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface UserApi {
    @POST("api/Musicians/register")
    suspend fun registerMusician(@Body user: RegisterBodyMusician): Response<Void>

    @POST("api/Organizer/register")
    suspend fun registerOrganizer(@Body user: RegisterBodyOrganizer): Response<Void>
}