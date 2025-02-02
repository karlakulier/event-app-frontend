package com.example.eventapp.models

data class Review(
    val id: Int,
    val author: String,
    val date: String,
    val rating: Int,
    val text: String,
    val likes: Int,
    val dislikes: Int
)

