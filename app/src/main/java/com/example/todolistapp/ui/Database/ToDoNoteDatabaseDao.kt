package com.example.todolistapp.ui.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolistapp.ui.Models.ToDoNoteItem

@Dao
interface ToDoNoteDatabaseDao {

    @Query("SELECT * FROM note_table ")
    fun readAllData(): LiveData<List<ToDoNoteItem>>
   @Insert
    suspend fun insertNotes(notes: ToDoNoteItem)
    @Delete
    suspend fun deleteNotes(notes: ToDoNoteItem)
    @Update
    suspend fun updateUser(notes: ToDoNoteItem)
}