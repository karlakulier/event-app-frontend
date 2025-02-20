package com.example.eventapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.musician_profile.IMusicService

class ArtistProfileViewModelFactory(private val musicService: IMusicService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArtistProfileViewModel::class.java)) {
            return ArtistProfileViewModel(musicService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
