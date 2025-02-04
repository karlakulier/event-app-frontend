package com.example.ws.models

data class SongData (
    val id: Int,
    val name: String,
    val linkYT: String?,
    val linkSpotify: String?,
    val musicianId: Int,
    val albumId: Int?,
    val epId: Int?
)