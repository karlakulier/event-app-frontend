package com.example.core.network.models

data class OrganizerProfile(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val contact: String,
    val birthDate: String,
    val review: String? = null,
    val userId: Int
)
