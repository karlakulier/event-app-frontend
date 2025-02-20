package com.example.eventapp.navigation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.example.core.login.Auth.loggedInUserData

import com.example.eventapp.viewmodels.GetEventViewModel
import com.example.eventapp.viewmodels.GetEventViewModelFactory
import com.example.ws.repository.EventRepository


@Composable
fun PastEventsForMusician(navController: NavController) {
    val eventRepository = remember { EventRepository() }
    val viewModel: GetEventViewModel = viewModel(
        factory = GetEventViewModelFactory(eventRepository)
    )

    val events by viewModel.events.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        loggedInUserData?.musician?.id?.let {
            viewModel.fetchPastEventsForMusician(it)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Header(navController)
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                events.isEmpty() -> Text(
                    text = "No past events available.",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(events) { event ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                }
                        ) {
                            EventCard(event = event, navController)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

