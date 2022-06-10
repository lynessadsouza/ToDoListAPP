package com.example.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.todolistapp.ui.ListPreviousNotes
import com.example.todolistapp.ui.theme.ToDoListAPPTheme


class HomeScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            ToDoListAPPTheme {
                //    LoadNotes()
                //   ToDoListHomeScreen()
                ListPreviousNotes()
            }
        }
    }
}