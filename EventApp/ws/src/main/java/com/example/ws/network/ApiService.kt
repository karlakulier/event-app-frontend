package com.example.ws.network

import com.example.core.network.models.SuccessfulResponseBody
import com.example.ws.models.LoggedInUserData
import com.example.ws.models.LoginBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/Auth/login")
    fun login(@Body loginBody: LoginBody): Call<LoggedInUserData>
}