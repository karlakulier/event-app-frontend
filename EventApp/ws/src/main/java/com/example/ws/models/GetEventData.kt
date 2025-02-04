package com.example.ws.models

import com.google.gson.annotations.SerializedName

data class GetEventData(
    @SerializedName("id") val id: Int,
    @SerializedName("dateTime") val dateTime: String,
    @SerializedName("description") val description: String,
    @SerializedName("finished") val finished: Boolean,
    @SerializedName("organizerId") val organizerId: Int,
    @SerializedName("location") val location: String,
    @SerializedName("musicGenre") val musicGenre: String,
    @SerializedName("relevanceScore") val relevanceScore: Int
)
