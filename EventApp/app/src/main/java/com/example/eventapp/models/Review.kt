package com.example.eventapp.models

data class Review(
    val id: Int,
    val text: String,
    val musicianGrade: Int,
    val organizerGrade: Int,
    val eventId: Int,
    val visitorId: Int,
    val musicianId: Int,
    val organizerId: Int
)

