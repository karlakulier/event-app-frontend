package com.example.ws.models

data class UserDto(
    val username: String,
    val password: String
)

data class OrganizatorDto(
    val id: Int? = null,
    val name: String,
    val surname: String,
    val email: String,
    val contact: String,
    val birthDate: String,
    val review: String,
    val userId: Int? = null
)

data class RegisterBodyOrganizer(
    val username: String,
    val password: String,
    val role: String = "Organizer",
    val organizator: OrganizatorDto
)


data class RegisterBodyMusician(
    val username: String,
    val password: String,
    val role: String = "Musician",
    val musician: MusicianDto
)

data class MusicianDto(
    val name: String,
    val surname: String,
    val email: String,
    val contact: String,
    val artistName: String,
    val description: String
)
