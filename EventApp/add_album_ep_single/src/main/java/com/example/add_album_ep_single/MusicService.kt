package com.example.add_album_ep_single

import com.example.core.musician_profile.IMusicService
import com.example.ws.models.AlbumData
import com.example.ws.models.EpData
import com.example.ws.models.SongData

import com.example.ws.network.ApiArtistProfile

class MusicService(private val api: ApiArtistProfile ) : IMusicService {

    override suspend fun addAlbum(album: Any): Result<Boolean> {
        val albumData = album as AlbumData
        val response = api.addAlbum(albumData)
        return if (response.isSuccessful) Result.success(true)
        else Result.failure(Exception("Failed to add album"))
    }

    override suspend fun addEp(ep: Any): Result<Boolean> {
        val epData = ep as EpData
        val response = api.addEP(epData)
        return if (response.isSuccessful) Result.success(true)
        else Result.failure(Exception("Failed to add EP"))
    }

    override suspend fun addSingle(single: Any): Result<Boolean> {
        val songData = single as SongData
        val response = api.addSong(songData)
        return if (response.isSuccessful) Result.success(true)
        else Result.failure(Exception("Failed to add single"))
    }
}