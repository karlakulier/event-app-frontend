package com.example.eventapp.models

data class OrganizerProfile(
    val id: Int,           // Dodano polje za id
    val name: String,
    val surname: String,
    val email: String,
    val contact: String,
    val birthDate: String,
    val review: String? = null, // review je opcionalno
    val userId: Int // Dodano polje za userId
)
