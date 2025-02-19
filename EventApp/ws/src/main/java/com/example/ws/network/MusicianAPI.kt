package com.example.ws.network

import com.example.core.network.models.MusicianProfile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface MusicianAPI {
    @GET("api/Musicians/GetMusician/{musicianId}")
    fun getMusicianById(@Path("musicianId") musicianId: Int): Call<MusicianProfile>


    @PUT("api/Musicians/UpdateProfile/{musicianId}")
    fun updateMusicianProfile(
        @Path("musicianId") musicianId: Int,
        @Body profile: MusicianProfile
    ): Call<String>
}