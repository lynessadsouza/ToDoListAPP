package com.example.todolistapp.ui.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
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
import com.example.todolistapp.ui.Database.ToDoNoteItem
import com.example.todolistapp.ui.Database.ToDoViewModel
import com.example.todolistapp.ui.theme.ToDoListAPPTheme

class AddTaskScreen() : ComponentActivity() {
    private val notesViewModel by viewModels<ToDoViewModel>()

    @OptIn(ExperimentalAnimationApi::class)
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListAPPTheme {
                Surface(color = MaterialTheme.colors.background) {
                    AddTask(notesViewModel)
                }
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun AddTask(model: ToDoViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val priority = listOf("High", "Medium", "Low")
    var expanded by remember { mutableStateOf(false) }
    var selectedPriority by remember { mutableStateOf(priority[1]) }

    Box(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
            .shadow(2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            val context = LocalContext.current
            Text(
                text = "Add Your Note Details Here ",
                fontWeight = FontWeight.Black, modifier = Modifier.padding(bottom = 20.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    Modifier.padding(bottom = 10.dp),
                    label = { Text(text = "Title") },
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        readOnly = true,
                        value = selectedPriority,
                        onValueChange = { },
                        label = { Text("Priority") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        priority.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedPriority = selectionOption
                                    expanded = false
                                }
                            ) {
                                Text(text = selectionOption)
                            }
                        }
                    }
                }
                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it

                    },
                    Modifier.padding(top = 10.dp),
                    label = { Text(text = "Description") },
                )
                Button(onClick = {
                    if (title == "" || description == "") {
                        Toast.makeText(
                            context,
                            "Please fill out all the details ",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val notes = ToDoNoteItem(0, title, selectedPriority, description, false)
                        model.addNote(notes)
                        Toast.makeText(context, "Note added successfully!", Toast.LENGTH_LONG)
                            .show()
                        context.startActivity(Intent(context, HomeScreen::class.java))
                    }
                }) {
                    Text("Add Task")
                }
            }
        }
    }
}
