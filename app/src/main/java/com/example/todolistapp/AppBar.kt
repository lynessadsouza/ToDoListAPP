package com.example.todolistapp.ui
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.todolistapp.R

@Composable
fun AppBar(
    onNavigationIconClick: () -> Unit
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle drawer"
                )

            }
        }

    )
}