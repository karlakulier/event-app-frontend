package com.example.ws.network

import com.example.core.network.models.PasswordProfile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface PasswordAPI {
    @GET("api/Password/GetUser/{userId}")
    fun getUserById(@Path("userId") userId: Int): Call<PasswordProfile>


    @PUT("api/Password/UpdatePassword/{userId}")
    fun updatePasswordProfile(
        @Path("userId") userId: Int,
        @Body profile: PasswordProfile
    ): Call<Unit>
}