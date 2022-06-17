package com.example.todolistapp.ui

import androidx.compose.runtime.*
import com.example.todolistapp.Notes

@Composable
fun NoteListArrays (){
    var noteListState by remember {
        mutableStateOf(
            listOf( Notes ("Drink water", "High", "Drink 2 Glasses"))

            /* "Drink water",
             "Walk",
             "run",
             "running"*/
            //  )
        )
    }

    var noteListNewCopy by remember {
        mutableStateOf(
            listOf( Notes ("hey", "High", "Drink 2 Glasses"),
                Notes (" water", "High", "Drink 2 Glasses"),
                Notes ("Drink ", "High", "Drink 2 Glasses")
                /*  "Drink water",
                  "Walk",
                  "run",
                  "running"*/
            )
        )
    }
}