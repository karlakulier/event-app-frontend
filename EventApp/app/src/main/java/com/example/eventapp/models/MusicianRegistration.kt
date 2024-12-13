package com.example.eventapp.models

data class MusicianRegistration(
    val displayName: String,
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val contact: String,
    val dateOfBirth: String
)