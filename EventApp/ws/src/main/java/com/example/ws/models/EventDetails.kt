package com.example.ws.models

import com.google.gson.annotations.SerializedName

data class EventDetails(
    @SerializedName("id") val id: Int,
    @SerializedName("dateTime") val dateTime: String,
    @SerializedName("description") val description: String,
    @SerializedName("organizerId") val organizerId: Int,
    @SerializedName("location") val location: String,
    @SerializedName("musicGenre") val musicGenre: String,
    @SerializedName("organizerName") val organizerName: String,
    @SerializedName("musicians") val musicians: List<String>,
    @SerializedName("averageRating") val averageRating: Double? = null,
    @SerializedName("reviews") val reviews: List<String> = emptyList()
)
