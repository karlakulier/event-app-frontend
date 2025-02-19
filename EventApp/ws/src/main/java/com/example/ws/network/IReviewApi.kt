package com.example.ws.network


import com.example.core.network.models.Review
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IReviewApi {

    @GET("api/ReviewByVisitor/event/{eventId}")
    suspend fun getReviewsByEvent(@Path("eventId") eventId: Int): List<Review>

    @POST("api/ReviewByVisitor/CreateReview")
    suspend fun createReview(@Body review: Review): Review
}