package com.example.eventapp.services

import com.example.eventapp.models.OrganizerProfile
import com.example.eventapp.models.UpdateProfileResult

interface IOrganizerService {

    suspend fun getOrganizerById(organizerId: Int): OrganizerProfile?

    suspend fun updateOrganizerProfile(organizerId: Int, profile: OrganizerProfile): UpdateProfileResult
}
