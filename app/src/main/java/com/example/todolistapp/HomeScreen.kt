package com.example.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import com.example.todolistapp.ui.ListPreviousNotes
import com.example.todolistapp.ui.theme.ToDoListAPPTheme


class HomeScreen : ComponentActivity() {

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            ToDoListAPPTheme {
                 ListPreviousNotes()

            }
        }
    }
}