package com.example.eventapp.navigation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.add_album_ep_single.MusicContentType

@Composable
fun AddNewAlbumEpSingleDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onContentSelected: (MusicContentType) -> Unit
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {},
            dismissButton = {},
            title = {
                Text(
                    text = "ADD NEW",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Column {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                onContentSelected(MusicContentType.ALBUM)
                                onDismiss()
                            }
                            .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(8.dp))
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Album", fontSize = 16.sp)
                        Icon(Icons.Default.ArrowForward, contentDescription = "Album")
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                onContentSelected(MusicContentType.EP)
                                onDismiss()
                            }
                            .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(8.dp))
                            .padding(vertical = 8.dp, horizontal = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("EP", fontSize = 16.sp)
                        Icon(Icons.Default.ArrowForward, contentDescription = "EP")
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                onContentSelected(MusicContentType.SINGLE)
                                onDismiss()
                            }
                            .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(8.dp))
                            .padding(vertical = 8.dp, horizontal = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Single", fontSize = 16.sp)
                        Icon(Icons.Default.ArrowForward, contentDescription = "Single")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Cancel", color = Color.White)
                    }
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

