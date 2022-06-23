package com.example.todolistapp.ui.Database

import androidx.lifecycle.LiveData
import com.example.todolistapp.ui.Models.ToDoNoteItem


class ToDoNoteRepository(private val noteDatabaseDao: ToDoNoteDatabaseDao) {
    val readAllData: LiveData<List<ToDoNoteItem>> = noteDatabaseDao.readAllData()

    suspend fun addNote(notes: ToDoNoteItem) {
        noteDatabaseDao.insertNotes(notes = notes)
    }

    suspend fun deleteNote(notes: ToDoNoteItem) {
        noteDatabaseDao.deleteNotes(notes = notes)
    }

    suspend fun updateUser(notes: ToDoNoteItem)
    {
        noteDatabaseDao.updateUser(notes)
    }


}