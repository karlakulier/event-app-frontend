package com.example.eventapp.navigation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.air.eventapplication.ApplicationModul
import com.air.eventapplication.models.ApplicationGet
import com.air.eventapplication.models.ApplicationMusician

@Composable
fun ApplicationCardForMusician(application: ApplicationMusician, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = application.nameOfEvnet,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {

                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = application.status,
                fontSize = 14.sp,
            )
        }
    }
}
@Composable
fun ApplicationsForMusician(navController: NavController,musicianId: Int) {
    var applications by remember { mutableStateOf<List<ApplicationMusician>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            applications = ApplicationModul.GetApplicationsForMusician(context, musicianId)
        } catch (e: Exception) {
            Log.d("ApplicationsForMusician", "Greška: ${e.message}")
        } finally {
            isLoading = false
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Header(navController)
        Text(
            text = "All Applications",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            if (isLoading) {
                Text(
                    text = "Loading...",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            } else if (applications.isEmpty()) {
                Text(
                    text = "No applications for event.",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(applications) { application ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { }
                        ) {
                            ApplicationCardForMusician(application = application, navController)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        ClassicFooter(navController)
    }

}