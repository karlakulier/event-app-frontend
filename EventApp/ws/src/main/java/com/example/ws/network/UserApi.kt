package com.example.ws.network

import com.example.ws.models.GetEventData
import com.example.ws.models.RegisterBodyOrganizer
import com.example.ws.models.RegisterBodyMusician
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface UserApi {
    @POST("api/Musicians/register")
    suspend fun registerMusician(@Body user: RegisterBodyMusician): Response<Void>

    @POST("api/Organizer/register")
    suspend fun registerOrganizer(@Body user: RegisterBodyOrganizer): Response<Void>

    @GET("api/Events/SortEventsBy")
    suspend fun getEvents(@Query("sortBy") sortBy: String): Response<List<GetEventData>>
}