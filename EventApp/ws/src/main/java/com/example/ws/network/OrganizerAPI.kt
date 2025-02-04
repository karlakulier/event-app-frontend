package com.example.ws.network

import com.example.core.network.models.OrganizerProfile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path


interface OrganizerAPI {

    @GET("api/Organizer/GetOrganizer/{organizerId}")
    fun getOrganizerById(@Path("organizerId") organizerId: Int): Call<OrganizerProfile>


    @PUT("api/Organizer/UpdateProfile/{organizerId}")
    fun updateOrganizerProfile(
        @Path("organizerId") organizerId: Int,
        @Body profile: OrganizerProfile
    ): Call<String>
}
