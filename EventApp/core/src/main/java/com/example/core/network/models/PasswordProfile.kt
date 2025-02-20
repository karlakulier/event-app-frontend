package com.example.core.network.models

data class PasswordProfile(
    val id: Int,
    val currentPassword: String,
    val newPassword: String,
    val confirmPassword: String,
)
