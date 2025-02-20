package com.example.ws.models

import com.google.gson.annotations.SerializedName

data class LoggedInUserData(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("role") val role: String,
    @SerializedName("musician") val musician: MusicianData?,
    @SerializedName("organizer") val organizer: OrganizerData?
)

data class OrganizerData(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("email") val email: String,
    @SerializedName("contact") val contact: String,
    @SerializedName("birthDate") val birthDate: String,
    @SerializedName("userId") val userId: Int
)

data class MusicianData(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("surname") val surname: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("contact") val contact: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("userId") val userId: Int?
)