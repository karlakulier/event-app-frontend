package com.example.core.googleLogin

import com.example.core.login.LoginHandler
import com.example.core.login.LoginOutcomeListener
import com.example.core.login.LoginUserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GoogleLoginHandler : LoginHandler {
    override fun handleLogin(loginToken: GoogleLoginToken, loginListener: LoginOutcomeListener) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = GoogleLoginRequestHandler.loginWithGoogle(loginToken.idToken)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val userResponse = response.body()
                        if (userResponse != null) {
                            loginListener.onSuccessfulLogin(
                                LoginUserData(
                                    id = userResponse.id,
                                    username = userResponse.username,
                                    role = userResponse.role,
                                    organizer = userResponse.organizer,
                                    musician = userResponse.musician
                                )
                            )
                        } else {
                            loginListener.onFailedLogin("No user data returned")
                        }
                    } else {
                        loginListener.onFailedLogin("Login failed: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    loginListener.onFailedLogin("Network error: ${e.localizedMessage}")
                }
            }
        }
    }
}
