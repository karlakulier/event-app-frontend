package com.example.ws.services

import android.util.Log
import com.example.core.network.models.Review
import com.example.core.review.IReviewService
import com.example.ws.network.IReviewApi


class ReviewService(private val reviewApi: IReviewApi) : IReviewService {
    override suspend fun getReviewsByEvent(eventId: Int): List<Review> {
        return try {
            reviewApi.getReviewsByEvent(eventId)
        } catch (e: Exception) {
            emptyList()
        }
    }
    override suspend fun createReview(review: Review): Review {
        return try {
            val response = reviewApi.createReview(review)
            // Logiraj uspješan odgovor
            Log.d("ReviewService", "Review submitted successfully: $response")
            Log.d("ReviewService", "Sending review: $review")

            response
        } catch (e: retrofit2.HttpException) {
            // Logiraj grešku ako server vraća HTTP error (npr. 400, 500)
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("ReviewService", "HTTP error: ${e.code()}, body: $errorBody", e)
            Log.d("ReviewService", "Sending review: $review")

            throw e
        } catch (e: Exception) {
            // Logiraj ostale greške (npr. network error)
            Log.e("ReviewService", "Unexpected error: ${e.message}", e)
            Log.d("ReviewService", "Sending review: $review")

            throw e
        }
    }
}