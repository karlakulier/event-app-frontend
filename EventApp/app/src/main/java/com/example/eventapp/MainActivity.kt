package com.example.eventapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

                    }
                }
            }
        }
    }
}
