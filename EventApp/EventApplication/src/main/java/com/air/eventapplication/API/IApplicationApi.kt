package com.air.eventapplication.API

import com.air.eventapplication.models.Application
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IApplicationApi {
    @POST("api/Applications/CreateApplication")
    suspend fun CreateApplication(@Body application: Application): Response<Void>

    /*@PUT("api/UpdateApplicationStatus/{id}")
    suspend fun azurirajStatus(
        @Path("id") idPrijave: String,
        @Body statusRequest: StatusRequest
    ): Response<Void>*/
}