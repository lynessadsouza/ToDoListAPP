package com.example.todolistapp

import android.content.Intent
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todolistapp.ui.theme.ToDoListAPPTheme
import kotlinx.coroutines.delay
import androidx.core.app.ActivityCompat.startActivityForResult
import android.R.id
import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts


@ExperimentalAnimationApi
@ExperimentalMaterialApi
class HomeScreen : ComponentActivity() {
    var title: String? = null
    var priority: String? = null
    var description: String? = null

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListAPPTheme {

                ListPreviousNotes()
                //Log.d("TAG", "setContent")
                //ShowData()
            }
        }
    }

    @Composable
    fun ShowData() {

        val context = LocalContext.current
        val intent = (context as HomeScreen).intent
        title = intent.getStringExtra("title")
        priority = intent.getStringExtra("priority")
        description = intent.getStringExtra("description")

        Log.d("HomeScreen", "$title, $priority , $description")
        val note = Notes(title, priority, description)


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
                        Notes("hey", "High", "Drink 2 Glasses"),
                        Notes(" water", "High", "Drink 2 Glasses"),
                        Notes("Drink ", "High", "Drink 2 Glasses"),
                        Notes("Drink ", "High", "Drink 2 Glasses")

                    )   )
            }
            var noteListNewCopy by remember {
                mutableStateOf(
                    listOf(
                        Notes("hey", "High", "Drink 2 Glasses"),
                        Notes(" water", "High", "Drink 2 Glasses"),
                        Notes("Drink ", "High", "Drink 2 Glasses"),
                        Notes("Drink ", "High", "Drink 2 Glasses")

                    )
                )
            }




            Row(
                Modifier
                    .padding(start = 5.dp, bottom = 5.dp, top = 5.dp, end = 10.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                val context = LocalContext.current
                val list = searchNote(noteListNewCopy)
                FloatingActionButton(
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Top)
                        .padding(15.dp),
                    shape = CircleShape,
                    onClick = {


                        val intent = Intent(context, AddTaskScreen::class.java)
                        startForResult.launch(intent)

                      //  context.startActivity(Intent(context, AddTaskScreen::class.java))
                        isButtonVisible = "true"

                        Log.d("TAG", isButtonVisible)
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_add_24),
                        contentDescription = null // decorative element
                    )
                }
                if (isButtonVisible == "true") {
                    Log.d("TAG", isButtonVisible)
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
            Text(
                text = "My ToDo Notes", fontWeight = FontWeight.Bold, modifier = Modifier
                    .padding(start = 20.dp, bottom = 10.dp)

            )
            DisplayNotesList(noteListState)
        }
    }


    @OptIn(ExperimentalAnimationApi::class)
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @Composable
    fun searchNote(
        listNotes: List<Notes>
    ): List<Notes> {
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
    fun DisplayNotesList(notes: List<Notes>) {
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
                        .background(Color.Blue),
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


                        notes[index].title?.let {
                            Text(
                                text = it,
                                color = if (checked) {
                                    Color.Blue
                                } else {
                                    Color.Black
                                }
                            )
                        }
                        Checkbox(checked, onCheckedChange)
                    }
                }
            }
        }
    }

    //onNewNoteAdded - like a listnr which will update
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
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data

           val note = intent?.getSerializableExtra("noteItem") as? Notes
            Log.d("TAG-intent","$note")

        }
    }

}

