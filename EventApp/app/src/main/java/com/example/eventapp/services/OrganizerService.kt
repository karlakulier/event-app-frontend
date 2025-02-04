package com.example.eventapp.services

import android.util.Log
import com.example.eventapp.models.OrganizerProfile
import com.example.eventapp.models.UpdateProfileResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Call

class OrganizerService(private val apiService: OrganizerAPI) : IOrganizerService {



    override suspend fun getOrganizerById(organizerId: Int): OrganizerProfile? {
        Log.d("OrganizerService", "Dohvaćam organizatora s ID-em: $organizerId") // Logirajte ID koji šaljete

        return withContext(Dispatchers.IO) {
            try {
                val response: Response<OrganizerProfile> =
                    apiService.getOrganizerById(organizerId).execute()
                if (response.isSuccessful) {
                    Log.d("OrganizerService", "Podaci dobiveni: ${response.body()}")
                    response.body()
                } else {
                    Log.e("OrganizerService", "Greška pri dobivanju podataka: ${response.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("OrganizerService", "Izuzetak pri dohvaćanju podataka: ${e.message}")
                null
            }
        }
    }

    override suspend fun updateOrganizerProfile(
        organizerId: Int,
        profileData: OrganizerProfile
    ): UpdateProfileResult {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.updateOrganizerProfile(organizerId, profileData).execute()

                if (response.isSuccessful) {
                    Log.d("OrganizerService", "Profile update successful: ${response.body()}")
                    return@withContext UpdateProfileResult(true, "Profile updated successfully")
                } else {
                    Log.e("OrganizerService", "Failed to update profile: ${response.message()}")
                    Log.e("OrganizerService", "Response Body: ${response.errorBody()?.string()}")
                    return@withContext UpdateProfileResult(
                        false,
                        "Failed to update profile: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("OrganizerService", "Exception while updating profile: ${e.message}")
                return@withContext UpdateProfileResult(false, "Error: ${e.message}")
            }
        }
    }
}
