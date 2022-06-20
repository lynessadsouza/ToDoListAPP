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


@ExperimentalAnimationApi
@ExperimentalMaterialApi
class HomeScreen : ComponentActivity() {

    var noteListState by mutableStateOf(
        listOf<Notes>(
            Notes("hey", "High", "Drink 2 Glasses"),
            Notes(" water", "High", "Drink 2 Glasses"),
            Notes("Drink ", "High", "Drink 2 Glasses"),
            Notes("Drink ", "High", "Drink 2 Glasses")
        )
    )
    var noteListNewCopy by mutableStateOf(
        listOf<Notes>(
            Notes("hey", "High", "Drink 2 Glasses"),
            Notes(" water", "High", "Drink 2 Glasses"),
            Notes("Drink ", "High", "Drink 2 Glasses"),
            Notes("Drink ", "High", "Drink 2 Glasses")
        )
    )


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

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val newNoteDetails = intent?.getSerializableExtra("noteItem") as Notes
                noteListState = noteListState + newNoteDetails
                noteListNewCopy = noteListNewCopy + newNoteDetails
            }
        }
}

