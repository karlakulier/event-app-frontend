package com.example.core.organizer

import com.example.core.network.models.OrganizerProfile
import com.example.core.network.models.UpdateProfileResult


interface IOrganizerService {

    suspend fun getOrganizerById(organizerId: Int): OrganizerProfile?

    suspend fun updateOrganizerProfile(organizerId: Int, profile: OrganizerProfile): UpdateProfileResult
}
