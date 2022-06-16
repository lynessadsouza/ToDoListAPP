package com.example.todolistapp

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Bottom
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.todolistapp.ui.theme.ToDoListAPPTheme


@ExperimentalAnimationApi
@ExperimentalMaterialApi
class HomeScreen : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var buttonClickedState by remember { mutableStateOf("") }

            ToDoListAPPTheme {
                ListPreviousNotes()

            }
        }
    }

    @ExperimentalMaterialApi
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun ListPreviousNotes() {
        //val (checked, onCheckedChange) = remember { mutableStateOf(false) }
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

            var noteListCopy: MutableList<String>
            Row(
                Modifier
                    .padding(5.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                val context = LocalContext.current
                val list = SearchNotefunc(noteListNewCopy)
                FloatingActionButton(

                  modifier=  Modifier.size(60.dp).align(Alignment.Bottom),
                    shape= CircleShape,

                    onClick = {isButtonVisible="true"

                        Log.d("TAG", isButtonVisible)},

                    ) {
                  //  Text("Add Note", fontSize = 10.em, ic)
                //    Icon(R.drawable.ic_baseline_add_24,contentDescription = "content description", tint=Color(0XFF0F9D58))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_add_24),
                        contentDescription = null // decorative element
                    )
                }

                if(isButtonVisible.equals("true"))
                {
                    Log.d("TAG", isButtonVisible)
                    AddNewNote { item ->
                    //updating state with added item
                    noteListState = noteListState + listOf(item)
                    noteListNewCopy = noteListNewCopy + listOf(item)
                        isButtonVisible="false"
                        Log.d("TAG", isButtonVisible)

                }

                }
                else
                {
                    //Dont do anything
                }



              //  Text("Add Note ", modifier = Modifier.padding(top = 12.dp))
                noteListCopy = noteListState as MutableList<String>
             //   val list = SearchNotefunc(noteListNewCopy)
                if (list.isNotEmpty()) {
                    noteListState = list
                } else if (list.isEmpty()) {
                    (noteListState as MutableList<String>).clear()
                    Toast.makeText(context, "Note not found ", Toast.LENGTH_LONG).show()
                }
            }
           // AnimatedVisibility(
             //   visible = checked,
             //   enter = fadeIn(),
             //   exit = shrinkOut(shrinkTowards = Alignment.BottomStart, animationSpec = tween(1000))
            //) {
               /* AddNewNote { item ->
                    //updating state with added item
                    noteListState = noteListState + listOf(item)
                    noteListNewCopy = noteListNewCopy + listOf(item)
                }*/
            //}
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


        LazyColumn() {
            items(notes.size) { index ->


                Card(
                    modifier = Modifier.padding(start = 20.dp,top=10.dp, bottom = 10.dp, end=20.dp).fillMaxWidth()
                    .background(Color.Blue),
                    shape = RoundedCornerShape(8.dp),
                    backgroundColor = MaterialTheme.colors.surface,
                ) {

                    Row(
                        modifier = Modifier
                            .padding(start = 10.dp, top = 1.dp, bottom = 1.dp, end = 10.dp)
                        //    .fillMaxSize()
                            .background(Color.White)
                            //.shadow(2.dp)
                            /*.border(
                                width = 1.dp,
                                color = Color.Blue,
                                shape = RoundedCornerShape(5.dp)
                            )*/
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
    }

    //onNewNoteAdded - like a listner which will update
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun AddNewNote(onNewNoteAdded: (String) -> Unit) {
        val openDialog = remember { mutableStateOf(true) }
        val (visible) = remember { mutableStateOf(true) }
        var text by remember { mutableStateOf("") }

     //   AnimatedVisibility(
        //    visible = visible,
         //   enter = slideInVertically(initialOffsetY = { 9000 * it }),
         //   exit = fadeOut()
       // ) {
            if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    title = {
                        Text(
                         //   modifier = Modifier.animateEnterExit(
                           //     enter = slideInVertically(
                            //        initialOffsetY = { 9000 * it },
                            //    ),
                            //    exit = slideOutVertically()
                         ///   ),

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
       // }
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
            label = { Text(text = "Search Keyword Here")},
        )
    }
}
