package com.example.todolistapp

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
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistapp.ui.MenuItem
import com.example.todolistapp.ui.navigationDrawerBody
import com.example.todolistapp.ui.navigationDrawerHeader
import com.example.todolistapp.ui.theme.ToDoListAPPTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
class HomeScreen : ComponentActivity() {
    var filteredNoteList by mutableStateOf(
        listOf(
            Notes("Water", "High", "Drink 2 Glasses")
         )
    )
    var listOfNotes by mutableStateOf(
        listOf(
            Notes("Water", "High", "Drink 2 Glasses"),
            Notes("Walk", "Medium", "Walk  "),
            Notes("Take a break ", "Low", " 5 Mins break "),
            Notes("Drink ", "High", "Drink water")
        )
    )
    var menuItems by mutableStateOf(
        listOf(
            MenuItem("home", "Home", "Go To Home Screen", Icons.Default.Home),
            MenuItem("deletedNotes", "Deleted Notes", "My Deleted Notes", Icons.Default.Delete),
            MenuItem("settings", "Settings", "Go To Home Screen  ",Icons.Default.Settings)
           )
    )

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListAPPTheme {
                val scaffold= rememberScaffoldState()
                val scope= rememberCoroutineScope()
                var textstate by remember { mutableStateOf("home")}
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
                                }
                            }
                        )
                    }

                ) {
                    if(textstate=="home"){
                        ListPreviousNotes()
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
                val list = searchNote(listOfNotes)
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
                    (filteredNoteList as MutableList<String>).clear()
                    Toast.makeText(context, "Note not found ", Toast.LENGTH_LONG).show()
                }
            }
            Text(
                text = "My ToDo Notes", fontWeight = FontWeight.Bold, modifier = Modifier
                    .padding(start = 20.dp, bottom = 10.dp)

            )
            displayNotes(filteredNoteList)
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @Composable
    fun searchNote(
        listNotes: List<Notes>
    ): MutableList<Notes> {
        var filteredList: MutableList<Notes> = arrayListOf()
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
                filteredList = (filteredList + listOf(noteItem)) as MutableList<Notes>
        }



        return filteredList
    }

    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun displayNotes(notes: List<Notes>) {
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
                            if(notes[index].priority.equals("High")){
                                ColorFilter.tint(Color.Red)
                            }
                            else if (notes[index].priority.equals("Medium")){
                                ColorFilter.tint(Color.Yellow)
                            }
                            else
                            {
                                ColorFilter.tint(Color.Green)
                            }
                        )
                        Column(

                            Modifier
                                .width(290.dp)
                                .padding(start = 10.dp)

                        ){
                            notes[index].title?.let {
                                Text(
                                    textAlign = TextAlign.Start,
                                    fontWeight=Bold,
                                    modifier= Modifier.padding(bottom = 5.dp),
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
                val newNoteDetails = intent?.getSerializableExtra("noteItem") as Notes
                filteredNoteList = filteredNoteList + newNoteDetails
                listOfNotes = listOfNotes + newNoteDetails
            }
        }
}

