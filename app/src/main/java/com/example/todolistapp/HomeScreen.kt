package com.example.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todolistapp.ui.theme.ToDoListAPPTheme

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListAPPTheme {
                LoadNotes()
            }
        }
    }
}

@Composable
fun LoadNotes() {
    Box(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize()
    ) {
        Column() {
            //AddNote()
            ListNotes(
                notesList = listOf(
                    "Drink water",
                    "Read Books",
                    "Eat fruits",
                    "Go for a Walk"
                )
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListNotes(
    notesList: List<String>
) {
    LazyColumn(
    ) {
        items(notesList.size) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp, bottom = 1.dp, end = 15.dp)
                    .fillMaxSize()
                    .background(Color.White)
                    .clip(RoundedCornerShape(10.dp))
                    .padding(15.dp),
            ) {
                Text(text = notesList[it], color = Color.Black)
            }
        }
    }
}



@Composable
fun AddNote(){
    val openDialog = remember { mutableStateOf(true) }
    var text by remember { mutableStateOf("") }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Add Note Description")
            },
            text = {
                Column() {
                    TextField(
                        value = text,
                        onValueChange = { text = it }
                    )
                    Text("Note description")
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { openDialog.value = false }
                    ) {
                        Text("Add Note To The List")
                    }
                }
            }
        )
    }
}

