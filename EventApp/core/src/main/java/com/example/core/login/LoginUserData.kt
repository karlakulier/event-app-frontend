    package com.example.core.login

data class LoginUserData(
    val id: Int,
    val username: String,
    val role: String,
    val organizer: OrganizerData?,
    val musician: MusicianData?
)


data class OrganizerData(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val contact: String,
    val birthDate: String,
    val userId: Int
)


data class MusicianData(
    val id: Int?,
    val name: String?,
    val surname: String?,
    val email: String?,
    val contact: String?,
    val description: String?,
    val artistName: String?,
    val userId: Int?
)
