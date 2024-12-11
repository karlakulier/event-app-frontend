package com.example.eventapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.eventapp.ui.theme.EventAppTheme
import com.example.eventapp.userInterface.StartScreen
import com.example.eventapp.userInterface.SignUp
import com.example.eventapp.userInterface.MusicianRegistrationScreen
import com.example.eventapp.userInterface.OrganizerRegistrationScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class MainActivity : ComponentActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            EventAppTheme {
                var currentScreen by remember { mutableStateOf("start") }
                var selectedRole by remember { mutableStateOf("") }

                Scaffold { innerPadding ->
                    when (currentScreen) {
                        "start" -> StartScreen(
                            modifier = Modifier.padding(innerPadding),
                            onSignUpClick = { currentScreen = "roleSelection" },
                            onLoginClick = { Log.d("Navigation", "Navigating to Login screen") },
                            onGoogleLoginClick = {
                                val signInIntent = googleSignInClient.signInIntent
                                startActivityForResult(signInIntent, RC_SIGN_IN)
                            }
                        )
                        "roleSelection" -> SignUp(
                            onNextClick = { role ->
                                selectedRole = role
                                when (role) {
                                    "Musician" -> currentScreen = "musicianRegistration"
                                    "Organizer" -> currentScreen = "organizerRegistration"
                                    else -> {
                                        Log.d("RoleSelection", "Selected role: $role")
                                        currentScreen = "start"
                                    }
                                }
                            },
                            onBackClick = { currentScreen = "start" }
                        )
                        "musicianRegistration" -> MusicianRegistrationScreen(
                            onBackClick = { currentScreen = "roleSelection" },
                            onRegisterClick = { registrationData ->
                                Log.d("Registration", "Musician data: $registrationData")
                                currentScreen = "start" // Navigacija na početni ekran nakon registracije
                            }
                        )
                        "organizerRegistration" -> OrganizerRegistrationScreen(
                            onBackClick = { currentScreen = "roleSelection" },
                            onRegisterClick = { registrationData ->
                                Log.d("Organizer Registration", "Organizer Data: $registrationData")
                                currentScreen = "start" // Ili idite na drugi ekran
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d("GoogleSignIn", "Signed in as: ${account.email}")
            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "Sign-In Failed: ${e.statusCode}")
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
