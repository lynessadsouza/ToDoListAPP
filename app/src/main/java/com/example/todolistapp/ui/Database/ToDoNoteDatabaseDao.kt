package com.example.todolistapp.ui.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoNoteDatabaseDao {

    @Query("SELECT * FROM deleted_note_table ")//where firstName= :name ")
    fun readdeletedAllData(): LiveData<List<ToDoNoteItemDeletedNote>>
    @Insert
    suspend fun insertDeletedNotes(notes: ToDoNoteItemDeletedNote)


    @Query("SELECT * FROM note_table ")//where firstName= :name ")
    fun readAllData(): LiveData<List<ToDoNoteItem>>

   @Insert
    suspend fun insertNotes(notes: ToDoNoteItem)

    @Delete
    suspend fun deleteNotes(notes: ToDoNoteItem)
    @Update
    suspend fun updateUser(notes: ToDoNoteItem)




}