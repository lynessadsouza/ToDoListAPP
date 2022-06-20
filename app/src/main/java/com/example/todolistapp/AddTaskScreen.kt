package com.example.todolistapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.todolistapp.ui.theme.ToDoListAPPTheme
import com.google.android.material.internal.ContextUtils.getActivity

class AddTaskScreen : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListAPPTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    AddTask()
                }
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun AddTask() {
    var title by remember { mutableStateOf("") }
    var description  by remember { mutableStateOf("") }
    val priority = listOf("High", "Medium", "Low")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(priority[0]) }


      Box(
         modifier = Modifier
             .padding(12.dp)
             .fillMaxSize()
             .shadow(2.dp)
             .clip(RoundedCornerShape(12.dp))
             .background(Color.White)
             .padding(12.dp),
    contentAlignment = Alignment.Center
      ){
       Column() {
           val context = LocalContext.current

           Text(text = "Add Your Note Details Here ", fontWeight = FontWeight.Black)
           Column() {
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
                       value = selectedOptionText,
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
                                   selectedOptionText = selectionOption
                                   Log.d("selectedOptionText", selectedOptionText)
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

                 //  setResult(Activity.Result_OK, yourIntentResult)
                   val intent = Intent()
                   var note=Notes(title, selectedOptionText, description)
                //   setResult(123,intent)
                   intent.putExtra("noteItem", note)
                   getActivity(context)?.setResult(Activity.RESULT_OK, intent);
                   getActivity(context)?.finish();

               }) {
                   Text("Click")
               }

           }
       }



          }
}
