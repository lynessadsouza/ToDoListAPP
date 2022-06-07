package com.example.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todolistapp.ui.theme.Shapes
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
                    .padding(15.dp)

                    //.animateItemPlacement(animationSpec = tween(10000000))
            ) {
                Text(text = notesList[it], color = Color.Black)
            }
        }
    }
}



