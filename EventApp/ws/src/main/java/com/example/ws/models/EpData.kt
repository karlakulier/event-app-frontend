package com.example.ws.models

data class EpData(
    val id: Int,
    val name: String,
    val releaseDate: String,
    val musicianId: Int,
    val existingSongs: List<SongData>,
    val newSongs: List<SongData>
)
