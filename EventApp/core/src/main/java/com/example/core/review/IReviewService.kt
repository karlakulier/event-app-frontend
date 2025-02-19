package com.example.core.review

import com.example.core.network.models.Review

interface IReviewService {
    suspend fun getReviewsByEvent(eventId: Int): List<Review>
    suspend fun createReview(review: Review): Review
}