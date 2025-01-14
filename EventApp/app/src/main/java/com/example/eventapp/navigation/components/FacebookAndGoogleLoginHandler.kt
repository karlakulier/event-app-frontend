package com.example.eventapp.navigation.components

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException

class FacebookAndGoogleLoginHandler(
    private val googleSignInLauncher: ActivityResultLauncher<Intent>,
    private val googleSignInClient: GoogleSignInClient,
    private val callbackManager: CallbackManager
) {

    fun handleGoogleLogin() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
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