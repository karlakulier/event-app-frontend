package com.example.eventapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ws.models.AlbumData
import com.example.ws.models.EpData
import com.example.ws.models.SongData
import com.example.ws.network.RetrofitClient.apiArtistProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArtistProfileViewModel : ViewModel() {

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
}