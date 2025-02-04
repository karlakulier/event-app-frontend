package com.example.ws.network

import com.example.ws.models.AlbumData
import com.example.ws.models.EpData
import com.example.ws.models.SongData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiArtistProfile {
    @GET("api/Music/GetAlbum/{musicianId}")
    suspend fun getAlbums(@Path("musicianId") musicianId: Int): Response<List<AlbumData>>

    @GET("api/Music/GetEp/{musicianId}")
    suspend fun getEPs(@Path("musicianId") musicianId: Int): Response<List<EpData>>

    @GET("api/Music/GetSongs/{musicianId}")
    suspend fun getSongs(@Path("musicianId") musicianId: Int): Response<List<SongData>>
}