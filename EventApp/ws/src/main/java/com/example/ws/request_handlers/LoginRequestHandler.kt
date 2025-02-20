package com.example.ws.request_handlers


import com.example.core.network.ResponseListener
import com.example.core.network.models.ErrorResponseBody
import com.example.core.network.models.SuccessfulResponseBody
import com.example.ws.models.LoggedInUserData
import com.example.ws.models.LoginBody
import com.example.ws.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRequestHandler(private val loginBody: LoginBody) {

    fun sendRequest(responseListener: ResponseListener<LoggedInUserData>) {
        val apiService = RetrofitClient.apiService
        val call = apiService.login(loginBody)

        call.enqueue(object : Callback<LoggedInUserData> {
            override fun onResponse(
                call: Call<LoggedInUserData>,
                response: Response<LoggedInUserData>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val userData = response.body()
                    responseListener.onSuccessfulResponse(userData!!)
                } else {
                    responseListener.onErrorResponse(ErrorResponseBody(response.code(), "No user data"))
                }
            }


            override fun onFailure(call: Call<LoggedInUserData>, t: Throwable) {
                responseListener.onNetworkFailure(t)
            }
        })
    }
}
