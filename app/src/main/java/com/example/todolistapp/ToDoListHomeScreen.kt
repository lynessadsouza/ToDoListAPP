package com.example.todolistapp.ui


import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
        var noteListState by remember { mutableStateOf(listOf("Drink water", "Walk")) }
        Row(
            Modifier
                .padding(5.dp)
                .align(alignment = Alignment.CenterHorizontally)
        ) {
            Checkbox(checked, onCheckedChange)
            Text("Add Note ", modifier = Modifier.padding(top = 12.dp))
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


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplayNotesList(notes: List<String>) {
    val listState = rememberScrollState()
    val (visible) = remember { mutableStateOf(true) }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        LazyColumn(
        ) {

            items(notes.size) { index ->
                Row(
                    //     modifier = Modifier.background(Color.Blue)
                    modifier = Modifier
                        .padding(start = 15.dp, top = 15.dp, bottom = 1.dp, end = 15.dp)
                        .fillMaxSize()
                        .background(Color.Gray)
                        .padding(15.dp)
                        .clickable {
                        }
                        .horizontalScroll(listState)
                        .animateEnterExit(
                            // Slide in/out the inner box.
                            enter = slideInVertically(
                                initialOffsetY = { 1000 * it },
                                animationSpec = tween(1000)
                            ),
                            exit = slideOutVertically()
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    val (checked, onCheckedChange) = remember { mutableStateOf(false) }
                    Text(
                        text = notes[index],
                        color = if (checked) {
                            Color.Red
                        } else {
                            Color.Black
                        }
                    )
                    Checkbox(checked, onCheckedChange)
                    //Text("Done")


                }


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
