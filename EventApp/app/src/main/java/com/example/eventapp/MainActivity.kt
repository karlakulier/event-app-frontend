package com.example.eventapp

import android.content.Intent
import PreviousEventsScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eventapp.navigation.components.PreviousEvents
import com.example.eventapp.navigation.components.ArtistProfileScreen
import com.example.eventapp.navigation.components.ChangePasswordScreen
import com.example.eventapp.navigation.components.EditProfileScreen
import com.example.eventapp.navigation.components.HomeScreen
import com.example.eventapp.navigation.components.LoginPage
import com.example.eventapp.ui.theme.EventAppTheme
import com.example.eventapp.userInterface.StartScreen
import com.example.eventapp.userInterface.SignUp
import com.example.eventapp.userInterface.MusicianRegistrationScreen
import com.example.eventapp.userInterface.OrganizerRegistrationScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            EventAppTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "loginAndRegistration"
                    ) {
                        composable("loginAndRegistration") {
                            LoginAndRegistration(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }
                        composable("registration") {
                            RegistrationScreen(navController = navController)
                        }
                        composable("signUp") {
                            SignUpScreen(navController = navController)
                        }
                        composable("controlNumber") {
                            ControlNumberScreen(navController = navController)
                        }
                        composable("home") {
                            HomeScreen(navController = navController)
                        }
                        composable ("loginPage"){
                            LoginPage(navController = navController)
                        }
                        composable("eventInputScreen") {
                            EventInputScreen(navController = navController)
                        }
                        composable("groupScreen") {
                            GroupsScreen(navController = navController)
                        }
                        composable("previousEvents") {
                            PreviousEvents(navController = navController)
                        }
                        composable("previousEventsScreen") {
                            PreviousEventsScreen(navController = navController)
                        }
                        composable("artistProfileScreen") {
                            ArtistProfileScreen(navController = navController)
                        }
                        composable("editProfileScreen") {
                            EditProfileScreen(
                                navController = navController,
                                onSaveChanges = {
                                    lifecycleScope.launch {
                                        snackbarHostState.showSnackbar("Changes saved successfully!")
                                    }
                                }
                            )
                        }
                        composable("changePasswordScreen") {
                            ChangePasswordScreen(
                                navController = navController,
                                onPasswordChangeSuccess = {
                                    lifecycleScope.launch {
                                        snackbarHostState.showSnackbar("Password saved successfully!")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
        //
        //
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
        callbackManager = CallbackManager.Factory.create()
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