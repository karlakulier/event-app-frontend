package com.example.eventapp.navigation.components

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.example.eventapp.models.GoogleLoginRequest
import com.example.eventapp.models.LoginResponse
import com.example.eventapp.services.RetrofitInstance
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FacebookAndGoogleLoginHandler(
    private val googleSignInLauncher: ActivityResultLauncher<Intent>,
    private val googleSignInClient: GoogleSignInClient,
    private val callbackManager: CallbackManager
) {

    fun handleGoogleLoginResult(
        intent: Intent,
        onSuccess: (LoginResponse) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        try {
            val account = task.getResult(ApiException::class.java)
            val token = account.idToken

            if (token != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = RetrofitInstance.api.googleLogin(GoogleLoginRequest(token))
                        withContext(Dispatchers.Main) {
                            onSuccess(response.data)
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            onError(e)
                        }
                    }
                }
            }
        } catch (e: ApiException) {
            onError(e)
        }
    }

    fun handleFacebookLogin() {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d("FacebookLogin", "Success: ${result.accessToken.token}")
            }

            override fun onCancel() {
                Log.d("FacebookLogin", "Cancelled")
            }

            override fun onError(error: FacebookException) {
                Log.e("FacebookLogin", "Error: ${error.message}")
            }
        })
    }
}