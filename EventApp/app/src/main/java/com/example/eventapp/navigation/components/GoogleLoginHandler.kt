package com.example.eventapp.navigation.components

import com.example.eventapp.models.GoogleLoginRequest
import com.example.eventapp.models.LoginResponse
import com.example.eventapp.services.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class GoogleLoginHandler(private val apiService: ApiService) {

    fun handleLogin(
        idToken: String,
        onSuccess: (LoginResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val loginRequest = GoogleLoginRequest(idToken)
                val response = apiService.googleLogin(loginRequest)

                withContext(Dispatchers.Main) {
                    onSuccess(response)
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    onError("Login failed: ${e.message}")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Network error: ${e.localizedMessage}")
                }
            }
        }
    }
}
