package com.example.eventapp.services

import com.example.eventapp.models.OrganizerProfile
import retrofit2.Response
import retrofit2.Call

class OrganizerService(private val apiService: OrganizerAPI) : IOrganizerService {


    override suspend fun getOrganizerById(organizerId: Int): OrganizerProfile? {
        val response: Response<OrganizerProfile> = apiService.getOrganizerById(organizerId).execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }


    override suspend fun updateOrganizerProfile(organizerId: Int, profileData: OrganizerProfile): Boolean {
        val response: Response<String> = apiService.updateOrganizerProfile(organizerId, profileData).execute()
        return response.isSuccessful
    }
}
