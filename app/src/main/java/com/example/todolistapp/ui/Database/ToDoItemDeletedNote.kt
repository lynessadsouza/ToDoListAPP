package com.example.todolistapp.ui.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "deleted_note_table")
data class ToDoNoteItemDeletedNote(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "note_title")
    val title: String?,
    @ColumnInfo(name = "note_priority")
    val priority: String?,
    @ColumnInfo(name = "note_description")
    val description: String?
): Serializable
{}