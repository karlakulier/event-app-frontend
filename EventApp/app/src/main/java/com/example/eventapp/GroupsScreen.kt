package com.example.eventapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eventapp.navigation.components.Header
import com.example.eventapp.navigation.components.OrganizerFooter
import com.example.eventapp.navigation.components.SearchBar


@Composable
fun GroupsScreen(navController: NavController) {
    val groups = listOf("Artist/Group 1", "Artist/Group 2", "Artist/Group 3")
    var selectedGroup by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {

        Header(navController)

        SearchBar()


        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {

                item {
                    Text(
                        text = "LIST OF REGISTERED GROUPS",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                items(groups) { groupName ->
                    GroupItem(
                        groupName = groupName,
                        onEvaluateClick = { selectedGroup = groupName },
                        onDetailsClick = {  }
                    )
                }
            }

            if (selectedGroup != null) {
                EvaluateModal(
                    groupName = selectedGroup!!,
                    onDismiss = { selectedGroup = null },
                    onSave = {  }
                )
            }
        }
        OrganizerFooter(navController)
    }
}

@Composable
fun GroupItem(
    groupName: String,
    onEvaluateClick: () -> Unit,
    onDetailsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = groupName, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Evaluate",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                modifier = Modifier.clickable { onEvaluateClick() }
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            imageVector = Icons.Default.ArrowCircleRight,
            contentDescription = "Details",
            modifier = Modifier
                .size(24.dp)
                .clickable { }
        )
    }
}
