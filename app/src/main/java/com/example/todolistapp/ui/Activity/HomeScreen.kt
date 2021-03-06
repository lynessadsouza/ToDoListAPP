package com.example.todolistapp.ui.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.todolistapp.NavigationDrawerHeader
import com.example.todolistapp.R
import com.example.todolistapp.ui.Activity.ui.TaskDetailsScreen
import com.example.todolistapp.ui.Database.ToDoViewModel
import com.example.todolistapp.ui.Models.BottomMenuContent
import com.example.todolistapp.ui.Models.MenuItem
import com.example.todolistapp.ui.Models.ToDoNoteItem
import com.example.todolistapp.ui.navigationDrawerBody
import com.example.todolistapp.ui.theme.ToDoListAPPTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
class HomeScreen : ComponentActivity() {
    private val notesViewModel by viewModels<ToDoViewModel>()

    private var filteredNoteList = emptyList<ToDoNoteItem>()
    private var deletedNotes = emptyList<ToDoNoteItem>()
    var listOfNotes by mutableStateOf(listOf<ToDoNoteItem>())
    private var databaseListNotes: MutableList<ToDoNoteItem> = ArrayList()
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
                var notDeletedNoteList = emptyList<ToDoNoteItem>()
                var deletedNoteList = emptyList<ToDoNoteItem>()

                if (note.isEmpty()) {
                    Log.d("tag", "no notes found ")
                    Toast.makeText(this, "Please enter  notes to proceed ", Toast.LENGTH_LONG)
                        .show()
                }
                databaseListNotes = note as MutableList<ToDoNoteItem>
                for (noteItem in databaseListNotes) {
                    if (noteItem.noteStatus == false) {
                        notDeletedNoteList = notDeletedNoteList + noteItem
                        filteredNoteList = notDeletedNoteList
                        listOfNotes = notDeletedNoteList
                    } else {
                        deletedNoteList = deletedNoteList + noteItem
                        deletedNotes = deletedNoteList
                    }
                }
            })

            ToDoListAPPTheme {
                val scaffold = rememberScaffoldState()
                val scope = rememberCoroutineScope()

                var defaultNavBarState by remember { mutableStateOf("home") }
                val context = LocalContext.current

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
                        NavigationDrawerHeader()
                        navigationDrawerBody(
                            menuItem = menuItems,
                            onItemClick = {
                                when (it.id) {
                                    "home" -> {
                                        defaultNavBarState = "home"
                                        scope.launch {
                                            scaffold.drawerState.close()
                                        }
                                    }
                                    "deletedNotes" -> {
                                        defaultNavBarState = "deletedNotes"
                                        scope.launch {
                                            scaffold.drawerState.close()
                                        }
                                    }
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                context.startActivity(Intent(context, AddTaskScreen::class.java))

                            },
                            backgroundColor = MaterialTheme.colors.primary,
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_baseline_add_24),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        )

                    }
                ) {
                    if (defaultNavBarState == "home") {
                        ListPreviousNotes()
                    } else if (defaultNavBarState == "deletedNotes") {
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
                if (list.isNotEmpty()) {
                    filteredNoteList = list
                } else if (list.isEmpty()) {
                    filteredNoteList = list
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
                        OvershootInterpolator(7f).getInterpolation(animation)
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
                    var context = LocalContext.current
                    Row(
                        modifier = Modifier
                            .padding(top = 1.dp, bottom = 1.dp, end = 10.dp)
                            .background(Color.White)
                            .clickable {
                                val intent = Intent(context, TaskDetailsScreen::class.java)
                                intent.putExtra("note", notes[index])
                                startActivity(intent)
                                Log.d("TAG-notes", "${notes[index]}")
                            }
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
                                        val note = ToDoNoteItem(
                                            notes[index].id,
                                            "${notes[index].title}",
                                            "${notes[index].priority}",
                                            "${notes[index].description}",
                                            true
                                        )
                                        model.updateNote(note)
                                    }
                                ),
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(Color.Black)
                        )

                        Checkbox(checked, onCheckedChange)
                        if (onCheckedChange.equals("true")) {
                            Log.d("onCheckedChange", notes[index].id.toString())
                        }
                    }
                }
            }
        }
    }

    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun displayDeletedNotes(notes: List<ToDoNoteItem>) {
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
            Text(
                text = "Your Deleted Notes Are listed below",
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
                                .background(Color.Red)
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
    fun listDeletedNotes(deletedNotes: List<ToDoNoteItem>) {
        displayDeletedNotes(deletedNotes)
    }

    @Composable
    fun BottomMenu(
        items:List<BottomMenuContent>,
        modifier : Modifier= Modifier,
        activeHighlightColor:Color= MaterialTheme.colors.onPrimary,
        activeTextColor: Color= Color.White,
         inactiveTextColor: Color= Color.Cyan,
        initialSelectedItemIndex:Int=0

    ){
        var selectedItemIndex by remember { mutableStateOf(initialSelectedItemIndex)}
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
                .padding(16.dp)
        ){
            items.forEachIndexed { index,item->

            }
        }
    }

    @Composable
    fun BottomMenuItem(
        items: BottomMenuContent,
        isSelected:Boolean=false,
        activeHighlightColor:Color= MaterialTheme.colors.onPrimary,
        activeTextColor: Color= Color.White,
        inactiveTextColor: Color= Color.Cyan,
        onItemClick:()->Unit
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier=Modifier.clickable {
                    onItemClick
                }
            ){
                Box( contentAlignment = Alignment.Center,
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    .background()
                    ){

                }
            }
    }
}

