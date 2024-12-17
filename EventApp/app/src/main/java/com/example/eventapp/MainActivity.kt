package com.example.eventapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eventapp.navigation.components.ArtistProfileScreen
import com.example.eventapp.navigation.components.HomeScreen
import com.example.eventapp.navigation.components.LoginPage
import com.example.eventapp.ui.theme.EventAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventAppTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "artistProfileScreen"
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
                        composable("artistProfileScreen") {
                            ArtistProfileScreen(navController = navController)
                        }

                    }
                }
            }
        }
    }
}
