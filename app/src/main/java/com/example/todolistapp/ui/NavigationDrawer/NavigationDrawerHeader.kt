package com.example.todolistapp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NavigationDrawerHeader() {
    Box(
            modifier = Modifier
                .background(Color.White)
        ) {
            Column() {
                    GreetingSection("Lynessa")
            }
        }
}

@Composable
fun GreetingSection(
    name: String
) {
    // here we just arrange the views
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(
            Modifier.padding(10.dp),
            verticalArrangement = Arrangement.Center
        ) {
                Text(
                    text = "Good Day, $name",
                    style = MaterialTheme.typography.h5
                )

        }
    }
}