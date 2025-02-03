package com.example.eventapp.services

import IReviewApi
import com.example.eventapp.models.ApiResponse
import com.example.eventapp.models.Review

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
            reviewApi.createReview(review)
        } catch (e: Exception) {
            throw e
        }
    }
}