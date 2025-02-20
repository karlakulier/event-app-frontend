package com.example.eventapp

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eventapp.navigation.components.LoginAndRegistrationNavigator
import com.example.ws.network.NetworkModule
import com.example.ws.repository.UserRepository
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import java.util.*
import android.util.Log
import androidx.activity.compose.setContent


class SignUpActivity : ComponentActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                } catch (e: ApiException) {
                    Log.e("GoogleSignIn", "Sign-In Failed: ${e.statusCode}")
                }
            }
        }

        setContent {
            val navController = rememberNavController()
            val userRepository = UserRepository(NetworkModule.userApi)

            LoginAndRegistrationNavigator(
                navController = navController,
                googleSignInLauncher = googleSignInLauncher,
                googleSignInClient = googleSignInClient,
                callbackManager = callbackManager,
                userRepository = userRepository
            )
        }
    }
}
