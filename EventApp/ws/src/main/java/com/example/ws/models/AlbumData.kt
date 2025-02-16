package com.example.ws.models

import java.util.Date


data class AlbumData(
    val name: String,
    val releaseDate: Date,
    val musicianId: Int,
    val existingSongs: List<SongData>,
    val newSongs: List<SongData>
)
