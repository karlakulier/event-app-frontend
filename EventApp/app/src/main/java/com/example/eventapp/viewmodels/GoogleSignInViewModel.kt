package com.example.eventapp.viewmodels


import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.activity.result.ActivityResultLauncher
import androidx.media3.common.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class GoogleSignInViewModel : ViewModel() {

    fun getSignInIntent(googleSignInClient: GoogleSignInClient): Intent {
        return googleSignInClient.signInIntent
    }

    fun signInWithGoogle(
        googleSignInClient: GoogleSignInClient,
        googleSignInLauncher: ActivityResultLauncher<Intent>
    ) {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    fun handleSignInResult(
        task: Task<GoogleSignInAccount>,
        onTokenReceived: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken

            if (idToken != null) {
                Log.d("GoogleSignIn", "ID Token: $idToken")
                onTokenReceived(idToken)
            } else {
                Log.e("GoogleSignIn", "ID Token not found")
                onError("ID Token not found")
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Sign-In Failed: ${e.statusCode}")
            onError("Sign-In Failed: ${e.statusCode}")
        }
    }
}






