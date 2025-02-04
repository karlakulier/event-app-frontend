package com.example.ws.request_handlers

import com.example.ws.network.GoogleApiService
import com.example.ws.network.RetrofitClient
import com.example.ws.models.GoogleLoginBody
import com.example.ws.models.GoogleLoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object GoogleLoginRequestHandler {

    fun loginWithGoogle(idToken: String, callback: (Response<GoogleLoginResponse>) -> Unit) {
        val call = RetrofitClient.apiService.googleLogin(GoogleLoginBody(idToken))

        call.enqueue(object : Callback<GoogleLoginResponse> {
            override fun onResponse(
                call: Call<GoogleLoginResponse>,
                response: Response<GoogleLoginResponse>
            ) {
                callback(response)
            }

            override fun onFailure(call: Call<GoogleLoginResponse>, t: Throwable) {
                callback(Response.error(500, okhttp3.ResponseBody.create(null, t.message ?: "Unknown error")))
            }
        })
    }
}