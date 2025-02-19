package com.example.ws.repository

import com.example.core.network.models.OrganizerProfile
import com.example.ws.network.OrganizerAPI
import com.example.ws.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class OrganizerRepository {
    private val organizerApi: OrganizerAPI = RetrofitClient.organizerApi

    suspend fun getOrganizerById(organizerId: Int): OrganizerProfile? {
        return withContext(Dispatchers.IO) {
            try {
                val response = organizerApi.getOrganizerById(organizerId).awaitResponse()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun updateOrganizerProfile(organizerId: Int, profile: OrganizerProfile): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = organizerApi.updateOrganizerProfile(organizerId, profile).awaitResponse()
                response.isSuccessful
            } catch (e: Exception) {
                false
            }
        }
    }
}
