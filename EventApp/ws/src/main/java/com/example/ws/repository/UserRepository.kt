package com.example.ws.repository

import android.os.Build
import com.example.ws.network.UserApi
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.ws.models.MusicianDto
import com.example.ws.models.OrganizatorDto
import com.example.ws.models.RegisterBodyMusician
import com.example.ws.models.RegisterBodyOrganizer
import com.example.ws.models.UserDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UserRepository(private val api: UserApi) {

    suspend fun registerOrganizer(
        username: String,
        password: String,
        name: String,
        surname: String,
        email: String,
        contact: String,
        birthDate: String
    ): Boolean {
        val organizatorDto = OrganizatorDto(
            name = name,
            surname = surname,
            email = email,
            contact = contact,
            birthDate = birthDate,
            review = "Default review"
        )
        val requestBody = RegisterBodyOrganizer(
            username = username,
            password = password,
            role = "Organizer",
            organizator = organizatorDto
        )

        val response = api.registerOrganizer(requestBody)

        Log.d("UserRepository", "Organizer Register Response: ${response.code()} - ${response.message()}")
        if (!response.isSuccessful) {
            Log.e("UserRepository", "Error Body: ${response.errorBody()?.string()}")
        }

        return response.isSuccessful
    }

    suspend fun registerMusician(
        username: String,
        password: String,
        name: String,
        surname: String,
        email: String,
        contact: String,
        artistName: String
    ): Boolean {
        val musicianDto = MusicianDto(
            name = name,
            surname = surname,
            email = email,
            contact = contact,
            artistName = artistName,
            description = "Default description" // Dodajemo default vrijednost
        )
        val requestBody = RegisterBodyMusician(
            username = username,
            password = password,
            role = "Musician",
            musician = musicianDto
        )

        val response = api.registerMusician(requestBody)

        Log.d("UserRepository", "Musician Register Response: ${response.code()} - ${response.message()}")
        if (!response.isSuccessful) {
            Log.e("UserRepository", "Error Body: ${response.errorBody()?.string()}")
        }

        return response.isSuccessful
    }
}