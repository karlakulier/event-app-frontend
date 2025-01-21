package com.example.eventapp

import PreviousEventsScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.eventapp.navigation.components.ChatListScreen
import com.example.eventapp.navigation.components.EditProfileScreen
import com.example.eventapp.navigation.components.HomeScreen
import com.example.eventapp.navigation.components.LoginPage
import com.example.eventapp.navigation.components.MessageListScreen
import com.example.eventapp.ui.theme.EventAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
                        //razvoj
                        composable("chatListScreen") {
                            ChatListScreen(navController = navController)
                        }
                        composable("messages/{chatId}") { backStackEntry ->
                            val chatId = backStackEntry.arguments?.getString("chatId")?.toIntOrNull()
                            if (chatId != null) {
                                Log.d("ChatId", "Navigated to chatId: $chatId")  // Provjera chatId
                                MessageListScreen(chatId = chatId)
                            }


                        }
                    }
                }
            }
        }
    }
}
