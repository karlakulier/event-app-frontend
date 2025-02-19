package com.example.core.password

import com.example.core.network.models.PasswordProfile
import com.example.core.network.models.UpdateProfileResult

interface IPasswordService {
    suspend fun getUserById(userId: Int): PasswordProfile?

    suspend fun updatePasswordProfile(userId: Int, profile: PasswordProfile): UpdateProfileResult
}