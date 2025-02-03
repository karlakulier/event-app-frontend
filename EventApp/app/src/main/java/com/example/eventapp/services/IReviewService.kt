package com.example.eventapp.services

import com.example.eventapp.models.Review

interface IReviewService {
    suspend fun getReviewsByEvent(eventId: Int): List<Review>
    suspend fun createReview(review: Review): Review
}