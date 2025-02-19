package com.example.core.musician

import com.example.core.network.models.MusicianProfile
import com.example.core.network.models.UpdateProfileResult

interface IMusicianService {
    suspend fun getMusicianById(musicianId: Int): MusicianProfile?

    suspend fun updateMusicianProfile(musicianId: Int, profile: MusicianProfile): UpdateProfileResult
}