package com.example.ws.services

import android.util.Log
import com.example.core.musician.IMusicianService
import com.example.core.network.models.MusicianProfile
import com.example.core.network.models.UpdateProfileResult
import com.example.ws.network.MusicianAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class MusicianService(private val apiService: MusicianAPI) : IMusicianService {

    override suspend fun getMusicianById(musicianId: Int): MusicianProfile? {
        Log.d("MusicianService", "Dohvaćam Musiciana s ID-em: $musicianId")

        return withContext(Dispatchers.IO) {
            try {
                val response =
                    apiService.getMusicianById(musicianId).execute()
                if (response.isSuccessful) {
                    Log.d("MusicianService", "Podaci dobiveni: ${response.body()}")
                    response.body()
                } else {
                    Log.e("MusicianService", "Greška pri dobivanju podataka: ${response.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("MusicianService", "Izuzetak pri dohvaćanju podataka: ${e.message}")
                null
            }
        }
    }

    override suspend fun updateMusicianProfile(
        musicianId: Int,
        profileData: MusicianProfile
    ): UpdateProfileResult {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.updateMusicianProfile(musicianId, profileData).execute()

                if (response.isSuccessful) {
                    Log.d("MusicianService", "Profile update successful: ${response.body()}")
                    return@withContext UpdateProfileResult(true, "Profile updated successfully")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("MusicianService", "Failed to update profile: $errorMsg")
                    Log.e("MusicianService", "Failed to update profile: ${response.message()}")
                    Log.e("MusicianService", "Response Body: ${response.errorBody()?.string()}")
                    return@withContext UpdateProfileResult(
                        false,
                        errorMsg
                    )
                }
            } catch (e: Exception) {
                Log.e("MusicianService", "Exception while updating profile: ${e.message}")
                return@withContext UpdateProfileResult(false, "Error: ${e.message}")
            }
        }
    }
}