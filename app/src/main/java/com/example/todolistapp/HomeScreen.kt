package com.example.todolistapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todolistapp.ui.ToDoListHomeScreen
import com.example.todolistapp.ui.theme.ToDoListAPPTheme

 var newList = ArrayList<String>()
class HomeScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            ToDoListAPPTheme {
            //    LoadNotes()
                ToDoListHomeScreen()
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
        Column {
            AddNotes()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddNotesToList(lazyListState: List<String>) {


    val listState = rememberScrollState()
    LazyColumn() {
        items(lazyListState.size) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp, bottom = 1.dp, end = 15.dp)
                    .fillMaxSize()
                    .horizontalScroll(listState)
                    .background(Color.White)
                    .clip(RoundedCornerShape(10.dp))
                    .padding(15.dp)
                    .animateItemPlacement(animationSpec = tween(1000)),
            ) {
                Text(
                    text = lazyListState[it], color = Color.Black,
                    modifier = Modifier
                        .align(
                            Alignment.BottomCenter
                        )
                        .animateItemPlacement(animationSpec = tween(10000))
                )
            }
        }
    }
}

//Custom dialog box func
@Composable
fun AddNewNote() {
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
                Column {
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
                    var addNoteButtonState by remember { mutableStateOf(false) }
                    if (addNoteButtonState) {
                        AddNewNote(noteDescription = text)
                    } else {
                        Box(contentAlignment = Alignment.Center) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                //Will have to add the add to list functionality here
                                onClick = {
                                    addNoteButtonState = true
                                    Log.d("Note Text", text)
                                    openDialog.value = false
                                }
                            ) {
                                Text("Add Note To The List")
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun AddNewNote(noteDescription: String) {
  /*  Log.d("noteDescription", noteDescription)
    AddNotesToList(lazyListState = listOf(noteDescription))*/
    val note="heyyhloo"
    val lazyListStatee by remember { mutableStateOf(listOf(note)) }
    AddNotesToList(lazyListState = lazyListStatee)


}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddNotes() {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(),
        exit = shrinkOut(shrinkTowards = Alignment.BottomStart, animationSpec = tween(1000))
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp)
                .shadow(2.dp)
        ) {
            Row(Modifier.padding(16.dp))
            {
                var buttonState by remember { mutableStateOf(false) }
                if (buttonState) {
                    val note="heyy"
                    val lazyListStatee by remember { mutableStateOf(listOf(note)) }
                    AddNotesToList(lazyListState = lazyListStatee)
                    AddNewNote()
                } else {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            buttonState = true
                        }
                    ) {
                        Text("Add New Note")
                    }
                }
            }
            val lazyListState by remember { mutableStateOf(listOf("Drink water", "Walk")) }
            AddNotesToList(lazyListState = lazyListState)
            val note="Run"
            val lazyListStatee by remember { mutableStateOf(listOf(note)) }
            AddNotesToList(lazyListState = lazyListStatee)

        }
    }
}

fun addtoList(lazyListState: List<String>)
{

}
