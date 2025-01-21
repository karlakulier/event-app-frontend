package com.example.eventapp.navigation.components

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.eventapp.userInterface.MusicianRegistrationScreen
import com.example.eventapp.userInterface.OrganizerRegistrationScreen
import com.example.eventapp.userInterface.SignUp
import com.example.eventapp.userInterface.StartScreen
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.facebook.login.LoginManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult



@Composable
fun LoginAndRegistrationNavigator(
    modifier: Modifier,
    navController: NavController,
    googleSignInLauncher: ActivityResultLauncher<android.content.Intent>,
    googleSignInClient: GoogleSignInClient,
    callbackManager: CallbackManager
) {
    var currentScreen by remember { mutableStateOf("start") }
    var selectedRole by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        when (currentScreen) {
            "start" -> StartScreen(
                modifier = Modifier.padding(innerPadding),
                onSignUpClick = { currentScreen = "roleSelection" },
                onLoginClick = { navController.navigate("login")},
                onGoogleLoginClick = {
                    // Prvo odjavi korisnika, pa pokreni login proces
                    googleSignInClient.signOut().addOnCompleteListener {
                        val signInIntent = googleSignInClient.signInIntent
                        googleSignInLauncher.launch(signInIntent)
                    }
                },
                onFacebookLoginClick = {
                    LoginManager.getInstance().registerCallback(callbackManager,
                        object : FacebookCallback<LoginResult> {
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
                    navController.navigate("home")
                }
            )
            "organizerRegistration" -> OrganizerRegistrationScreen(
                onBackClick = { currentScreen = "roleSelection" },
                onRegisterClick = { registrationData ->
                    Log.d("Organizer Registration", "Organizer Data: $registrationData")
                    navController.navigate("home")
                }
            )
        }
    }
}