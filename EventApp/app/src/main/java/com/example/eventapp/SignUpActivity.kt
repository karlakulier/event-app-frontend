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
/*
@Composable
fun SignUpScreen(navController : NavController) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }

    val showDatePicker = {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context, // Prosljeđivanje konteksta ovdje
            { _, selectedYear, selectedMonth, selectedDay ->
                dob = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            },
            year,
            month,
            day
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Registration", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Surname") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail address") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = contact,
            onValueChange = { contact = it },
            label = { Text("Contact") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = dob,
                onValueChange = { dob = it },
                label = { Text("Date of birth") },
                modifier = Modifier.weight(1f),
                enabled = false // Onemogućuje unos direktno
            )
            IconButton(onClick = showDatePicker) {
                Image(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Calendar Icon"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = { navController.navigate("controlNumber") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(56.dp)
        ) {
            Text("Sign up", style = MaterialTheme.typography.bodyLarge)
        }
    }
}*/
