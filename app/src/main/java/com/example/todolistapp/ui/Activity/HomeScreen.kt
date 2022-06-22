package com.example.todolistapp.ui.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import com.example.todolistapp.HomeScreenHeader
import com.example.todolistapp.R
import com.example.todolistapp.ui.Database.ToDoNoteItem
import com.example.todolistapp.ui.Database.ToDoNoteItemDeletedNote
import com.example.todolistapp.ui.Database.ToDoViewModel
import com.example.todolistapp.ui.Models.MenuItem
import com.example.todolistapp.ui.navigationDrawerBody
import com.example.todolistapp.ui.theme.ToDoListAPPTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
class HomeScreen : ComponentActivity() {
    private var filteredNoteList = emptyList<ToDoNoteItem>()
    private var deletedNotes = emptyList<ToDoNoteItemDeletedNote>()
 //private var listOfNotes = emptyList<ToDoNoteItem>()

    val notesViewModel by viewModels<ToDoViewModel>()

    var listOfNotes by mutableStateOf(
        listOf(
            ToDoNoteItem(0, "Water", "High", "Drink 2 Glasses")
        )
    )
    var menuItems by mutableStateOf(
        listOf(
            MenuItem("home", "Home", "Go To Home Screen", Icons.Default.Home),
            MenuItem("deletedNotes", "Trash", "My Deleted Notes", Icons.Default.Delete),
            MenuItem("settings", "Settings", "Go To Home Screen  ", Icons.Default.Settings)
        )
    )

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            notesViewModel.readAllData.observe(this, Observer { note ->
                filteredNoteList = note
                listOfNotes = note


            })

            notesViewModel.readDeletedData.observe(this, Observer { note ->
                deletedNotes = note
            })

            displayNotes(notes = filteredNoteList, model = notesViewModel)
            ToDoListAPPTheme {
                val scaffold = rememberScaffoldState()
                val scope = rememberCoroutineScope()
                var textstate by remember { mutableStateOf("home") }
                Scaffold(
                    scaffoldState = scaffold,
                    topBar = {
                        com.example.todolistapp.ui.AppBar(
                            onNavigationIconClick = {
                                scope.launch {
                                    scaffold.drawerState.open()
                                }
                            }
                        )
                    },
                    drawerContent = {
                        // navigationDrawerHeader()
                        HomeScreenHeader()
                        navigationDrawerBody(
                            menuItem = menuItems,
                            onItemClick = {
                                when (it.id) {
                                    "home" -> textstate = "home"
                                    "deletedNotes" -> textstate = "deletedNotes"
                                }
                            }
                        )
                    }
                ) {
                    if (textstate == "home") {
                        ListPreviousNotes()
                    } else if (textstate == "deletedNotes") {
                        listDeletedNotes(deletedNotes)
                    }
                }
            }
        }
    }

    @ExperimentalMaterialApi
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun ListPreviousNotes() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(5.dp)
                .shadow(1.dp)

        ) {
            Row(
                Modifier
                    .padding(start = 5.dp, bottom = 5.dp, top = 5.dp, end = 10.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                val context = LocalContext.current
                val list = searchNote(listNotes = listOfNotes)
                FloatingActionButton(
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Top)
                        .padding(15.dp),
                    shape = CircleShape,
                    onClick = {
                        val intent = Intent(context, AddTaskScreen::class.java)
                        startForResult.launch(intent)
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_add_24),
                        contentDescription = null // decorative element
                    )
                }

                if (list.isNotEmpty()) {
                    filteredNoteList = list
                } else if (list.isEmpty()) {
                    filteredNoteList=list
                    //    (filteredNoteList as MutableList<String>).clear()
                    Toast.makeText(context, "Note not found ", Toast.LENGTH_LONG).show()
                }
            }
            Text(
                text = "My ToDo Notes", fontWeight = Bold, modifier = Modifier
                    .padding(start = 20.dp, bottom = 10.dp)
            )
            displayNotes(filteredNoteList, notesViewModel)
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @Composable
    fun searchNote(
        listNotes: List<ToDoNoteItem>
    ): MutableList<ToDoNoteItem> {
        var filteredList: MutableList<ToDoNoteItem> = arrayListOf()
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
            if (noteItem.title?.contains(searchText) == true)
                filteredList = (filteredList + listOf(noteItem)) as MutableList<ToDoNoteItem>
        }
        return filteredList
    }

    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun displayNotes(notes: List<ToDoNoteItem>, model: ToDoViewModel) {
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
                        .shadow(1.dp)
                        .fillMaxWidth()
                        .background(Color.White),
                    shape = RoundedCornerShape(8.dp),
                    backgroundColor = MaterialTheme.colors.surface,
                ) {
                    Row(
                        modifier = Modifier
                            .padding(top = 1.dp, bottom = 1.dp, end = 10.dp)
                            .background(Color.White)
                            .horizontalScroll(listState),
                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        val (checked, onCheckedChange) = remember { mutableStateOf(false) }
                        Image(
                            painter = painterResource(R.drawable.priority),
                            contentDescription = "",
                            colorFilter =
                            if (notes[index].priority.equals("High")) {
                                ColorFilter.tint(Color.Red)
                            } else if (notes[index].priority.equals("Medium")) {
                                ColorFilter.tint(Color.Yellow)
                            } else {
                                ColorFilter.tint(Color.Green)
                            }
                        )
                        Column(

                            Modifier
                                .width(250.dp)
                                .padding(start = 10.dp)

                        ) {
                            notes[index].title?.let {
                                Text(
                                    textAlign = TextAlign.Start,
                                    fontWeight = Bold,
                                    modifier = Modifier.padding(bottom = 5.dp),
                                    text = it,
                                    color = if (checked) {
                                        Color.Blue
                                    } else {
                                        Color.Black
                                    }
                                )
                            }
                            notes[index].description?.let {
                                Text(
                                    fontSize = 13.sp,
                                    text = it,
                                    color = if (checked) {
                                        Color.Blue
                                    } else {
                                        Color.Black
                                    }

                                )
                            }

                        }
                        Image(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .clickable(
                                    onClick = {
                                        notes[index].title
                                        Log.d("tag", notes[index].id.toString())

                                        val note = ToDoNoteItem(
                                            notes[index].id,
                                            "${notes[index].title}",
                                            "${notes[index].priority}",
                                            "${notes[index].description}"
                                        )
                                        val noteItem = ToDoNoteItemDeletedNote(
                                            notes[index].id,
                                            "${notes[index].title}",
                                            "${notes[index].priority}",
                                            "${notes[index].description}"
                                        )
                                        model.addDeletedNote(noteItem)
                                        model.deleteUser(note)


                                    }
                                ),
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                        Checkbox(checked, onCheckedChange)
                    }
                }
            }
        }
    }

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val newNoteDetails = intent?.getSerializableExtra("noteItem") as ToDoNoteItem
                //      filteredNoteList = filteredNoteList + newNoteDetails
                //    listOfNotes = listOfNotes + newNoteDetails
            }
        }


    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun displayDeletedNotes(notes: List<ToDoNoteItemDeletedNote> ) {
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
        Column() {
            Text(text = "Your Deleted Notes Are listed below",
                fontWeight = Bold
            )
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
                            .shadow(1.dp)
                            .fillMaxWidth()
                            .background(Color.White),
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = MaterialTheme.colors.surface,
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(top = 1.dp, bottom = 1.dp, end = 10.dp)
                                .background(Color.White)
                                .horizontalScroll(listState),
                            verticalAlignment = Alignment.CenterVertically

                        ) {

                            Image(
                                painter = painterResource(R.drawable.priority),
                                contentDescription = "",
                                colorFilter =
                                if (notes[index].priority.equals("High")) {
                                    ColorFilter.tint(Color.Red)
                                } else if (notes[index].priority.equals("Medium")) {
                                    ColorFilter.tint(Color.Yellow)
                                } else {
                                    ColorFilter.tint(Color.Green)
                                }
                            )
                            Column(

                                Modifier
                                    .width(250.dp)
                                    .padding(start = 10.dp)

                            ) {
                                notes[index].title?.let {
                                    Text(
                                        textAlign = TextAlign.Start,
                                        fontWeight = Bold,
                                        modifier = Modifier.padding(bottom = 5.dp),
                                        text = it

                                    )
                                }
                                notes[index].description?.let {
                                    Text(
                                        fontSize = 13.sp,
                                        text = it

                                    )
                                }

                            }


                        }
                    }
                }
            }
        }
    }

    @Composable
    fun listDeletedNotes(deletedNotes:List<ToDoNoteItemDeletedNote>) {
        displayDeletedNotes(deletedNotes)
    }
}

