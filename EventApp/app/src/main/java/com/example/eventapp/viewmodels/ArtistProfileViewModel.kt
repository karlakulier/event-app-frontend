package com.example.eventapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.login.Auth
import com.example.core.musician_profile.IMusicService
import com.example.ws.models.AlbumData
import com.example.ws.models.EpData
import com.example.ws.models.SongData
import com.example.ws.network.RetrofitClient.apiArtistProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArtistProfileViewModel(private val musicService: IMusicService) : ViewModel() {

    private val _albums = MutableStateFlow<List<AlbumData>>(emptyList())
    val albums: StateFlow<List<AlbumData>> = _albums

    private val _eps = MutableStateFlow<List<EpData>>(emptyList())
    val eps: StateFlow<List<EpData>> = _eps

    private val _songs = MutableStateFlow<List<SongData>>(emptyList())
    val songs: StateFlow<List<SongData>> = _songs

    fun fetchAlbums(musicianId: Int) {
        viewModelScope.launch {
            val response = apiArtistProfile.getAlbums(musicianId)
            if (response.isSuccessful) {
                _albums.value = response.body() ?: emptyList()
            }
        }
    }

    fun fetchEPs(musicianId: Int) {
        viewModelScope.launch {
            val response = apiArtistProfile.getEPs(musicianId)
            if (response.isSuccessful) {
                _eps.value = response.body() ?: emptyList()
            }
        }
    }

    fun fetchSongs(musicianId: Int) {
        viewModelScope.launch {
            val response = apiArtistProfile.getSongs(musicianId)
            if (response.isSuccessful) {
                _songs.value = response.body() ?: emptyList()
            }
        }
    }

    fun addAlbum(album: AlbumData) {
        viewModelScope.launch {
            val result = musicService.addAlbum(album)
            result.onSuccess {
                Auth.loggedInUserData?.musician?.id?.let { musicianId ->
                   fetchAlbums(musicianId)
                    fetchEPs(musicianId)
                    fetchSongs(musicianId)
                }
                Log.d("ArtistProfileViewModel", "Album added successfully")
            }.onFailure { error ->
                // Došlo je do greške
                Log.e("ArtistProfileViewModel", "Error adding album: ${error.message}")
            }
        }
    }

    fun addEp(ep: EpData) {
        viewModelScope.launch {
            val result = musicService.addEp(ep)
            result.onSuccess {
                Auth.loggedInUserData?.musician?.id?.let { musicianId ->
                    fetchAlbums(musicianId)
                    fetchEPs(musicianId)
                    fetchSongs(musicianId)
                }
                Log.d("ArtistProfileViewModel", "EP added successfully")
            }.onFailure { error ->
                Log.e("ArtistProfileViewModel", "Error adding EP: ${error.message}")
            }
        }
    }

    fun addSingle(single: SongData) {
        viewModelScope.launch {
            val result = musicService.addSingle(single)
            result.onSuccess {
                Auth.loggedInUserData?.musician?.id?.let { musicianId ->
                    fetchAlbums(musicianId)
                    fetchEPs(musicianId)
                    fetchSongs(musicianId)
                }
                Log.d("ArtistProfileViewModel", "Single added successfully")
            }.onFailure { error ->
                Log.e("ArtistProfileViewModel", "Error adding single: ${error.message}")
            }
        }
    }
}