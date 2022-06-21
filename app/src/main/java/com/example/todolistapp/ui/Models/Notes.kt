package com.example.todolistapp.ui.Models
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Notes(

    val id: Int?,
    val title: String?,
    val priority: String?,
    val description: String ?): Serializable