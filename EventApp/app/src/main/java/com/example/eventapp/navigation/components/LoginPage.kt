package com.example.eventapp.navigation.components

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.core.login.LoginHandler
import com.example.eventapp.R
import com.example.eventapp.viewmodels.LoginViewModel
import com.example.login_username_password.UsernamePasswordLoginHandler
import com.example.login_username_password.UsernamePasswordLoginToken

@Composable
fun LoginPage(
    navController: NavController,
    loginHandler: LoginHandler = UsernamePasswordLoginHandler(),
) {
    val viewModel: LoginViewModel = viewModel()
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var awaitingResponse by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = stringResource(id = R.string.logo_desc),
            modifier = Modifier
                .width(250.dp)
                .height(60.dp)
                .padding(top = 16.dp)
        )

        // Naslov "Login"
        Text(
            text = "Login",
            fontSize = 45.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 130.dp)
        )

        // Prikaz poruke o grešci (ako postoji)
        if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Polja za unos
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            TextField(
                value = username,
                onValueChange = { viewModel.updateUsername(it) },
                label = { Text("E-mail") },
                singleLine = true,
                modifier = Modifier.width(320.dp)
            )

            TextField(
                value = password,
                onValueChange = { viewModel.updatePassword(it) },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.width(320.dp)
            )
        }

        // Mali razmak između polja i gumba
        Spacer(modifier = Modifier.height(40.dp))

        // Gumb za login
        Button(
            onClick = {
                awaitingResponse = true
                val loginToken = UsernamePasswordLoginToken(username, password)

                viewModel.login(
                    loginHandler,
                    loginToken,
                    onSuccessfulLogin = {
                        awaitingResponse = false
                        navController.navigate("home")
                    },
                    onFailedLogin = {
                        awaitingResponse = false
                    }
                )
            },
            enabled = !awaitingResponse,
            colors = ButtonDefaults.buttonColors(Color.DarkGray),
            modifier = Modifier
                .width(270.dp)
                .height(40.dp)
        ) {
            Text(if (awaitingResponse) "Logging in..." else "Login")
        }
    }
}
