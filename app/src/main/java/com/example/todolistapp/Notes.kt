package com.example.todolistapp

import java.io.Serializable

data class Notes(
    val title: String?,
    val priority: String?,
    val description: String ?): Serializable

