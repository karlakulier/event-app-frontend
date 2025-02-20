package com.example.eventapp.userInterface

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventapp.R

@Composable
fun SignUp(
    onNextClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedRole by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = stringResource(id = R.string.app_logo),
            modifier = Modifier
                .size(280.dp)

        )


        Text(text = "Sign Up", fontSize = 30.sp, modifier = Modifier.padding(bottom = 24.dp))


        Text(text = "Choose your role", fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

        listOf("Organizer", "Musician", "Visitor").forEach { role ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(
                        color = if (role == selectedRole) Color(0xFFE0E0E0) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        selectedRole = role
                    }
                    .padding(16.dp)
            ) {
                Text(text = role, textAlign = TextAlign.Center)
            }
        }


        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { if (selectedRole.isNotEmpty()) onNextClick(selectedRole) },
            enabled = selectedRole.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(text = "Next")
        }


        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = { onBackClick() },
            modifier = Modifier
                .width(150.dp)
                .height(48.dp)
                .border(1.dp, Color.Red, MaterialTheme.shapes.small),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Back",
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

