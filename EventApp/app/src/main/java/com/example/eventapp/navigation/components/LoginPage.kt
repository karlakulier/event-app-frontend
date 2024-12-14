package com.example.eventapp.navigation.components

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eventapp.R

class LoginActivity: AppCompatActivity(){

}

@Composable
fun LoginPage(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // Logo Image
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = stringResource(id = R.string.logo_desc),
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp)
                    .padding(top = 16.dp)
            )

            // Login Header
            Text(
                text = "Login",
                fontSize = 45.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 130.dp)
            )

            // Input Box for Email and Password
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                // Email Input
                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("E-mail") },
                    singleLine = true,
                    modifier = Modifier
                        .width(320.dp)
                        .height(50.dp)
                )

                // Password Input
                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .width(320.dp)
                        .height(50.dp)
                )
            }
        }

        // Login Button at Bottom
        Button(
            onClick = {
                navController.navigate("home") // Navigacija na početnu stranicu
            },
            colors = ButtonDefaults.buttonColors(Color.DarkGray),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(270.dp)
                .height(60.dp)
                .padding(bottom = 32.dp)
        ) {
            Text("Login")
        }
    }
}