package com.example.eventapp

import PreviousEventsScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.eventapp.navigation.components.ApplicationsForEvent
import com.example.eventapp.navigation.components.PreviousEvents
import com.example.eventapp.navigation.components.ArtistProfileScreen
import com.example.eventapp.navigation.components.ChangePasswordScreen
import com.example.eventapp.navigation.components.ChatListScreen
import com.example.eventapp.navigation.components.EditProfileScreen
import com.example.eventapp.navigation.components.FacebookAndGoogleLoginHandler
import com.example.eventapp.navigation.components.HomeScreen
import com.example.eventapp.navigation.components.LoginAndRegistrationNavigator
import com.example.eventapp.navigation.components.LoginPage
import com.example.eventapp.navigation.components.MessageListScreen
import com.example.eventapp.navigation.components.OrganizerProfile
import com.example.eventapp.navigation.components.OrganizerProfileViewOnly
import com.example.ws.network.OrganizerRetrofit
import com.example.ws.services.OrganizerService
import com.example.eventapp.ui.theme.EventAppTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.facebook.FacebookSdk
import com.facebook.CallbackManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    private val isLoggedIn = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        val googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    Log.d("GoogleSignIn", "Signed in as: ${account?.email}")
                    isLoggedIn.value = true
                } catch (e: ApiException) {
                    Log.e("GoogleSignIn", "Sign-In Failed: ${e.statusCode}")
                }
            }
        }

        val loginHandler = FacebookAndGoogleLoginHandler(
            googleSignInLauncher = googleSignInLauncher,
            googleSignInClient = googleSignInClient,
            callbackManager = callbackManager
        )

        setContent {
            EventAppTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(key1 = isLoggedIn.value) {
                    if (isLoggedIn.value) {
                        navController.navigate("home") {
                            popUpTo("loginAndRegistration") { inclusive = true }
                        }
                    }
                }
                Scaffold(modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "loginAndRegistration"
                    ) {
                        composable("loginAndRegistration") {
                            LoginAndRegistrationNavigator(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController,
                                googleSignInLauncher = googleSignInLauncher,
                                googleSignInClient = googleSignInClient,
                                callbackManager = callbackManager
                            )
                        }
                        composable("login") {
                            LoginPage(navController = navController)
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
                        composable("loginPage") {
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

                        composable("organizerProfileViewOnly/{organizerId}") { backStackEntry ->
                            val organizerId = backStackEntry.arguments?.getString("organizerId")?.toInt() ?: 1
                            val organizerService = OrganizerService(OrganizerRetrofit.api)
                            OrganizerProfileViewOnly(
                                organizerId = organizerId,
                                organizerService = organizerService,
                                onEditClick = {
                                    navController.navigate("organizerProfile/$organizerId")
                                }
                            )
                        }

                        composable("organizerProfile/{organizerId}") { backStackEntry ->
                            val organizerId = backStackEntry.arguments?.getString("organizerId")?.toInt() ?: 1
                            val organizerService = OrganizerService(OrganizerRetrofit.api)

                            // Prosljeđivanje navController u OrganizerProfile
                            OrganizerProfile(
                                organizerId = organizerId,
                                organizerService = organizerService,
                                navController = rememberNavController()
                            )
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
                        composable("chatListScreen") {
                            ChatListScreen(navController = navController)
                        }
                        composable("messages/{chatId}") { backStackEntry ->
                            val chatId =
                                backStackEntry.arguments?.getString("chatId")?.toIntOrNull()
                            if (chatId != null) {
                                Log.d("ChatId", "Navigated to chatId: $chatId")  // Provjera chatId
                                MessageListScreen(chatId = chatId)
                            }
                        }
                        composable("applicationsForEvent/{eventId}") { backStackEntry ->
                            val eventId =
                                backStackEntry.arguments?.getString("eventId")?.toIntOrNull()
                            if (eventId != null) {
                                Log.d("ChatId", "Navigated to chatId: $eventId")  // Provjera chatId
                                ApplicationsForEvent(navController, eventId)
                            }
                        }
                    }
                }
            }
        }
    }
}