package com.example.todolistapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todolistapp.ui.theme.ToDoListAPPTheme


@ExperimentalAnimationApi
@ExperimentalMaterialApi
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

    @ExperimentalMaterialApi
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun ListPreviousNotes() {
        val (checked, onCheckedChange) = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(5.dp)
                .shadow(1.dp)

        ) {
            HomeScreenHeader()
            var noteListState by remember {
                mutableStateOf(
                    listOf(
                        "Drink water",
                        "Walk",
                        "run",
                        "running"
                    )
                )
            }
            val filteredList : MutableList<String> = arrayListOf()
            Row(
                Modifier
                    .padding(5.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                Checkbox(checked, onCheckedChange)
                Text("Add Note ", modifier = Modifier.padding(top = 12.dp))
               /* SearchNote { item ->
                    Log.d("TAG", item)
                    for (noteItem in noteListState) {
                        if (noteItem.contains(item)) {
                            filteredList = listOf(noteItem) as MutableList<String>
                            Log.d("filteredList", "$filteredList")
                    //    DisplayNotesList(notes = filteredList)
                        }
                    }
                }*/
                SearchNotefunc(noteListState)
            }
            AnimatedVisibility(
                visible = checked,
                enter = fadeIn(),
                exit = shrinkOut(shrinkTowards = Alignment.BottomStart, animationSpec = tween(1000))
            ) {
                AddNewNote { item ->
                    //updating state with added item
                    noteListState = noteListState + listOf(item)
                }
            }
            DisplayNotesList(noteListState)
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @Composable
    fun SearchNotefunc( noteListState: List<String>) {
        var filteredList : MutableList<String> = arrayListOf()
        var searchText by remember { mutableStateOf("") }
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            label = { Text(text = "Search Keyword Here") },
        )
        for (noteItem in noteListState) {
            if (noteItem.contains(searchText)) {
                filteredList = (filteredList+listOf(noteItem)) as MutableList<String>
                Log.d("filteredList", "$filteredList")
                //DisplayNotesList(notes = listOf("hey", "hello"))
            }
        }
    }

    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun DisplayNotesList(notes: List<String>) {
        val listState = rememberScrollState()
        LazyColumn() {
            items(notes.size) { index ->
                Row(
                    modifier = Modifier
                        .padding(start = 15.dp, top = 15.dp, bottom = 1.dp, end = 15.dp)
                        .fillMaxSize()
                        .background(Color.White)
                        .shadow(2.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Blue,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(15.dp)
                        .clickable {}
                        .horizontalScroll(listState)
                        .animateItemPlacement(
                            // Slide in/out the inner box.
                            tween(durationMillis = 250)
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    val (checked, onCheckedChange) = remember { mutableStateOf(false) }
                    Text(
                        modifier = Modifier.animateItemPlacement(tween(durationMillis = 1000)),
                        text = notes[index],
                        color = if (checked) {
                            Color.Red
                        } else {
                            Color.Black
                        }
                    )
                    Checkbox(checked, onCheckedChange)
                }
            }
        }
    }

    //onNewNoteAdded - like a listner which will update
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun AddNewNote(onNewNoteAdded: (String) -> Unit) {
        val openDialog = remember { mutableStateOf(true) }
        val (visible) = remember { mutableStateOf(true) }
        var text by remember { mutableStateOf("") }

        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(initialOffsetY = { 9000 * it }),
            exit = fadeOut()
        ) {
            if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    title = {
                        Text(
                            modifier = Modifier.animateEnterExit(
                                enter = slideInVertically(
                                    initialOffsetY = { 9000 * it },
                                ),
                                exit = slideOutVertically()
                            ),

                            text = "Add Note Description"


                        )
                    },
                    text = {
                        Column() {
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
                            val addNoteButtonState by remember { mutableStateOf(false) }
                            if (addNoteButtonState) {
                                onNewNoteAdded(text)
                            } else {
                                Box(contentAlignment = Alignment.Center) {
                                    Button(
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = {
                                            if (text != "") {
                                                onNewNoteAdded(text)
                                            }
                                            //  addNoteButtonState = true

                                            openDialog.value = false
                                        }
                                    ) {
                                        Text(
                                            "Add Note To The List",
                                        )
                                    }
                                }
                            }
                        }
                    },
                )
            }
        }
    }

    @Composable
    fun SearchNote(onNoteSearched: (String) -> Unit) {
        var searchText by remember { mutableStateOf("") }

        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                onNoteSearched(searchText)
            },
            label = { Text(text = "Search Keyword Here") },
        )
    }
}