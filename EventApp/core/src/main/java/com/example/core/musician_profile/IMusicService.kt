package com.example.core.musician_profile

interface IMusicService {
    suspend fun addAlbum(album: Any): Result<Boolean>
    suspend fun addEp(ep: Any): Result<Boolean>
    suspend fun addSingle(single: Any): Result<Boolean>
}