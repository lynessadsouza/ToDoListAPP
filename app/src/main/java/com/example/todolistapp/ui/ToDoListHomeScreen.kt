package com.example.todolistapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ToDoListHomeScreen() {
    Column(modifier = Modifier.fillMaxWidth()) {
        var noteListstate by remember { mutableStateOf(listOf("Drink water", "Walk")) }

        NewNoteInput { item ->
            noteListstate = noteListstate + listOf(item)
        }
        NewNoteInputList(noteListstate)

    }
}


@Composable
fun NewNoteInput(
    onNewNoteAdded: (String) -> Unit
) {
    var textState by remember { mutableStateOf("hey") }
    //  TextField(value = textState, onValueChange = { textState = it })
    /* TextField(
         value = textState,
         onValueChange = { textState = it }
     )*/

    Column() {
        Button(

            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),

            onClick = {
                onNewNoteAdded(textState)
            }
        ) {
            Text("Add New Note")
        }
    }
}

@Composable
fun NewNoteInputList(notes: List<String>) {
    //val noteList by remember { mutableStateOf(listOf("Drink water", "Walk")) }
    //AddNotesToList(lazyListState = lazyListState)

    LazyColumn {
        items(notes.size) { index ->
            Text(text = notes[index])

        }
    }
}