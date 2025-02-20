package com.air.eventapplication.API


import com.air.eventapplication.models.Application
import com.air.eventapplication.models.ApplicationGet
import com.air.eventapplication.models.ApplicationMusician
import com.air.eventapplication.models.ApplicationUpdateStatus
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IApplicationApi {
    @POST("api/Applications/CreateApplication")
    suspend fun CreateApplication(@Body application: Application): Response<Void>

    @PUT("api/Applications/UpdateStatus")
    suspend fun UpdateApplicationStatus(
        @Body applicationUpdateStatus: ApplicationUpdateStatus
    ): Response<Void>

    @GET("api/Applications/ByEvent/{id}")
    suspend fun GetApplicationsByEvent(
        @Path("id") id: Int
    ): Response<List<ApplicationGet>>

    @GET("api/Applications/ByMusician/{id}")
    suspend fun GetApplicationsForMusician(
        @Path("id") id: Int
    ): Response<List<ApplicationMusician>>
}