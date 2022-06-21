package com.example.todolistapp.ui.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.todolistapp.ui.Models.Notes
import com.example.todolistapp.ui.theme.ToDoListAPPTheme
import com.google.android.material.internal.ContextUtils.getActivity

class AddTaskScreen() : ComponentActivity() {
    val notesViewModel by viewModels<ToDoViewModel>()

    @OptIn(ExperimentalAnimationApi::class)
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ToDoListAPPTheme {

                // A surface container using the 'background' color from the theme
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
                        Log.d("title", title)
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
                        Log.d("description", description)

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
                       // val note = Notes(0,title, selectedPriority, description)

                        val notes = ToDoNoteItem(0, title, selectedPriority, description)
                        model.addNote(notes)
                        Log.d("description", "Note added")

                        val intent = Intent()
                        intent.putExtra("noteItem", notes)
                        getActivity(context)?.setResult(Activity.RESULT_OK, intent);
                        getActivity(context)?.finish();
                    }
                }) {
                    Text("Add Task")
                }
            }
        }
    }
}
