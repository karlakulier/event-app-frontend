package com.example.eventapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.air.eventapplication.ApplicationModul
import com.air.eventapplication.models.Application
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.runBlocking



@Composable
fun LoginAndRegistration(modifier: Modifier = Modifier,navController: NavController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*Logo Image
        Image(
            painter = painterResource(id = R.drawable.logo), // Zamijeni 'logo' imenom slike koja se nalazi u dokumentu drawable
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp)
        )*/

        val context = LocalContext.current

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "New user", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { navController.navigate("registration")},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign up")
        }

        Spacer(modifier = Modifier.height(24.dp))


        Text(text = "Already have account", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /* TODO: Handle login */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Color.Gray)
            )
            Text(
                text = " or ",
                modifier = Modifier.padding(horizontal = 8.dp),
                color = Color.Gray
            )
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Color.Gray)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}