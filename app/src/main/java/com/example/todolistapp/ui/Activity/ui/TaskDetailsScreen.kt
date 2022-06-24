package com.example.todolistapp.ui.Activity.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.ui.core.Text
import com.example.todolistapp.ui.Activity.HomeScreen
import com.example.todolistapp.ui.Activity.ui.ui.theme.ToDoListAPPTheme
import com.example.todolistapp.ui.Models.ToDoNoteItem


class TaskDetailsScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListAPPTheme {

                getNoteDetails()
              AddTask()

            }
        }
    }

    private fun getNoteDetails() {
        val intent = intent
        val newNoteDetails = intent?.getSerializableExtra("note") as ToDoNoteItem
        val title =newNoteDetails.title
        Log.d("TAG","${newNoteDetails.title}")
    }

}

@Composable
fun AddTask( ) {
    Text(text = "hey")
}
