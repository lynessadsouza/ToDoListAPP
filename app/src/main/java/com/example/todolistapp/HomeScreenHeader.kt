package com.example.todolistapp

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreenHeader() {

  /*  val visible by remember { mutableStateOf(true) }
  */
    Box(
            modifier = Modifier
                .background(Color.White)
        ) {
            Column() {


           //     AnimatedVisibility(
            //        visible = visible,
             //       enter = fadeIn() + slideInHorizontally(
             //           initialOffsetX = { -it },
              //          animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy)
               //     ),
               //     exit = shrinkOut(shrinkTowards = Alignment.BottomStart, animationSpec = tween(1000))

              //  ){
                    GreetingSection("Lynessa")
              //  }
             
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
        val visible by remember { mutableStateOf(true) }

        val context = LocalContext.current
        Column(
            Modifier.padding(10.dp),
            verticalArrangement = Arrangement.Center
        ) {

            AnimatedVisibility(
                visible = visible ,
                enter = fadeIn() +  expandHorizontally (),
                exit = fadeOut()+ shrinkHorizontally ()

           ) {
                // heading text view
                Text(
                    text = "Good morning, $name",
                    style = MaterialTheme.typography.h5
                )
                /*Text(
                    text = "We wish you have a good day!",
                    style = MaterialTheme.typography.body1
                )*/
           }
        }
    }
}