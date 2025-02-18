package com.example.ws.models

import java.util.Date


data class AlbumData(
    val id: Int,
    val name: String,
    val releaseDate: String,
    val musicianId: Int,
    val existingSongs: List<SongData>,
    val newSongs: List<SongData>
)
