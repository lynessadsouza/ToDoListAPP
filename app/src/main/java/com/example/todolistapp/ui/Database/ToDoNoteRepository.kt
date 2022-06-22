package com.example.todolistapp.ui.Database

import androidx.lifecycle.LiveData


class ToDoNoteRepository(private val noteDatabaseDao: ToDoNoteDatabaseDao) {
    val readAllData: LiveData<List<ToDoNoteItem>> = noteDatabaseDao.readAllData()
    val readDeletedData: LiveData<List<ToDoNoteItemDeletedNote>> =
        noteDatabaseDao.readdeletedAllData()

    suspend fun addDeletedNote(notes: ToDoNoteItemDeletedNote) {
        noteDatabaseDao.insertDeletedNotes(notes = notes)
    }

    suspend fun addNote(notes: ToDoNoteItem) {
        noteDatabaseDao.insertNotes(notes = notes)
    }

    suspend fun deleteNote(notes: ToDoNoteItem) {
        noteDatabaseDao.deleteNotes(notes = notes)
    }

}