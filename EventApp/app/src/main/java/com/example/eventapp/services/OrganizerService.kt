package com.example.eventapp.services

import android.util.Log
import com.example.eventapp.models.OrganizerProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Call

class OrganizerService(private val apiService: OrganizerAPI) : IOrganizerService {


    override suspend fun getOrganizerById(organizerId: Int): OrganizerProfile? {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<OrganizerProfile> = apiService.getOrganizerById(organizerId).execute()
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
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val response = apiService.updateOrganizerProfile(organizerId, profileData).execute()
            response.isSuccessful
        }
    }
}
