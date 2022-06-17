package com.example.todolistapp

import android.os.Bundle
import android.util.Log
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todolistapp.ui.theme.ToDoListAPPTheme
import kotlinx.coroutines.delay


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
        var isButtonVisible by remember { mutableStateOf("") }
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
            var noteListNewCopy by remember {
                mutableStateOf(
                    listOf(
                        "Drink water",
                        "Walk",
                        "run",
                        "running"
                    )
                )
            }
            Row(
                Modifier
                    .padding(start = 5.dp, bottom = 5.dp, top = 5.dp, end = 10.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                val scale = remember { androidx.compose.animation.core.Animatable(0f) }
                val context = LocalContext.current
                val list = SearchNotefunc(noteListNewCopy)
                LaunchedEffect(key1 = true) {
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = {
                                OvershootInterpolator(2f).getInterpolation(it)
                            }
                        )
                    )
                    delay(3000L)
                }

                FloatingActionButton(
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Top)
                        .scale(scale.value)
                        .padding(15.dp),
                    shape = CircleShape,
                    onClick = {
                        isButtonVisible = "true"

                        Log.d("TAG", isButtonVisible)
                    },

                    ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_add_24),
                        contentDescription = null // decorative element
                    )
                }

                if (isButtonVisible.equals("true")) {
                    Log.d("TAG", isButtonVisible)
                    AddNewNote { item ->
                        //updating state with added item
                        noteListState = noteListState + listOf(item)
                        noteListNewCopy = noteListNewCopy + listOf(item)
                        isButtonVisible = "false"
                        Log.d("TAG", isButtonVisible)

                    }

                } else {
                    Log.d("TAG", "Dont do anything ")

                }
                if (list.isNotEmpty()) {
                    noteListState = list
                } else if (list.isEmpty()) {
                    (noteListState as MutableList<String>).clear()
                    Toast.makeText(context, "Note not found ", Toast.LENGTH_LONG).show()
                }
            }
            DisplayNotesList(noteListState)
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @Composable

    fun SearchNotefunc(
        listNotes: List<String>
    ): List<String> {
        var filteredList: MutableList<String> = arrayListOf()
        var searchText by remember { mutableStateOf("") }
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            Modifier.padding(bottom = 35.dp),
            label = { Text(text = "Search Keyword Here") },
        )
        for (noteItem in listNotes) {
            if (noteItem.contains(searchText)) {
                filteredList = (filteredList + listOf(noteItem)) as MutableList<String>
            }
        }
        return filteredList
    }

    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun DisplayNotesList(notes: List<String>) {
        val listState = rememberScrollState()
        val scale = remember { androidx.compose.animation.core.Animatable(0f) }
        LaunchedEffect(key1 = true) {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = { animation ->
                        OvershootInterpolator(2f).getInterpolation(animation)
                    }
                )
            )
            delay(3000L)
        }

        LazyColumn(modifier = Modifier.scale(scale.value)) {

            items(notes.size) { index ->
                Card(
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            top = 10.dp,
                            bottom = 10.dp,
                            end = 20.dp
                        )
                        .fillMaxWidth()
                        .background(Color.Blue),
                    /*.scale(scale.value),*/
                    shape = RoundedCornerShape(8.dp),
                    backgroundColor = MaterialTheme.colors.surface,
                ) {
                    Row(
                        modifier = Modifier
                            .padding(start = 10.dp, top = 1.dp, bottom = 1.dp, end = 10.dp)
                            .background(Color.White)
                            .clickable {}
                            .horizontalScroll(listState),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        val (checked, onCheckedChange) = remember { mutableStateOf(false) }


                        Text(
                            text = notes[index],
                            color = if (checked) {
                                Color.Blue
                            } else {
                                Color.Black
                            }
                        )
                        Checkbox(checked, onCheckedChange)
                    }
                }
            }
        }
    }

    //onNewNoteAdded - like a listner which will update
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun AddNewNote(onNewNoteAdded: (String) -> Unit) {
        val scale = remember { androidx.compose.animation.core.Animatable(0f) }
        val openDialog = remember { mutableStateOf(true) }
        var text by remember { mutableStateOf("") }

        LaunchedEffect(key1 = true) {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = {
                        OvershootInterpolator(2f).getInterpolation(it)
                    }
                )
            )
            delay(3000L)
        }


        if (openDialog.value) {
            AlertDialog(
                modifier = Modifier.scale(scale.value),
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(
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