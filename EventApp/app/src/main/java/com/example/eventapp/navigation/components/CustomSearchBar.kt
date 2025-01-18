package com.example.eventapp.navigation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun CustomSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(), // Ispunjava cijeli prostor roditelja
        contentAlignment = Alignment.Center // Centriranje sadržaja unutar Box-a
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.99f) // Širina 90% roditeljskog prostora
                .height(35.dp) // Visina
                .clip(RoundedCornerShape(20.dp)) // Zaobljeni rubovi
                .border(1.dp, Color.Black, RoundedCornerShape(20.dp)) // Crni obrub na zaobljenim rubovima
                .background(Color.White), // Bijela pozadina
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // Unutarnji razmak
                verticalAlignment = Alignment.CenterVertically // Centriranje elemenata vertikalno
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Black, // Crna boja ikone
                    modifier = Modifier.size(20.dp) // Veličina ikone
                )
                Spacer(modifier = Modifier.width(8.dp)) // Razmak između ikone i teksta
                BasicTextField(
                    value = query,
                    onValueChange = { onQueryChanged(it) },
                    textStyle = TextStyle(
                        color = Color.Black, // Crni tekst
                        fontSize = 16.sp // Veličina fonta
                    ),
                    singleLine = true, // Ograniči tekst na jedan red
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text(
                                text = "Search",
                                color = Color.Gray, // Placeholder boja
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    },
                    modifier = Modifier.fillMaxWidth() // Tekst zauzima ostatak prostora
                )
            }
        }
    }
}


