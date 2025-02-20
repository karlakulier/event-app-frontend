package com.example.ws.services

import android.util.Log
import com.example.core.password.IPasswordService
import com.example.core.network.models.PasswordProfile
import com.example.core.network.models.UpdateProfileResult
import com.example.ws.network.PasswordAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PasswordService(private val apiService: PasswordAPI) : IPasswordService {

    override suspend fun getUserById(userId: Int): PasswordProfile? {
        Log.d("PasswordService", "Dohvaćam Usera s ID-em: $userId")

        return withContext(Dispatchers.IO) {
            try {
                val response =
                    apiService.getUserById(userId).execute()
                if (response.isSuccessful) {
                    Log.d("PasswordService", "Podaci dobiveni: ${response.body()}")
                    response.body()
                } else {
                    Log.e("PasswordService", "Greška pri dobivanju podataka: ${response.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("PasswordService", "Izuzetak pri dohvaćanju podataka: ${e.message}")
                null
            }
        }
    }

    override suspend fun updatePasswordProfile(
        userId: Int,
        profileData: PasswordProfile
    ): UpdateProfileResult {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.updatePasswordProfile(userId, profileData).execute()

                if (response.isSuccessful) {
                    Log.d("PasswordService", "Password update successful: ${response.body()}")
                    return@withContext UpdateProfileResult(true, "Password updated successfully")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("PasswordService", "Failed to update password: $errorMsg")
                    Log.e("PasswordService", "Failed to update password: ${response.message()}")
                    Log.e("PasswordService", "Response Body: ${response.errorBody()?.string()}")
                    return@withContext UpdateProfileResult(
                        false,
                        errorMsg
                    )
                }
            } catch (e: Exception) {
                Log.e("PasswordService", "Exception while updating password: ${e.message}")
                return@withContext UpdateProfileResult(false, "Error: ${e.message}")
            }
        }
    }

}