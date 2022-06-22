package com.example.todolistapp.ui.Database


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<ToDoNoteItem>>
    private val repository: ToDoNoteRepository

    val readDeletedData: LiveData<List<ToDoNoteItemDeletedNote>>


    init {
        val userDao = ToDoDatabase.getDatabase(application).noteDao()
        repository = ToDoNoteRepository(userDao)
        readAllData = repository.readAllData
        readDeletedData=repository.readDeletedData
    }

    fun addDeletedNote(noteItem: ToDoNoteItemDeletedNote) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addDeletedNote(noteItem)
        }

    }

    fun addNote(noteItem: ToDoNoteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNote(noteItem)
        }

    }


    fun deleteNote(noteItem: ToDoNoteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(notes = noteItem)
        }
    }

    fun updateNote(noteItem: ToDoNoteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(notes = noteItem)
        }

    }

}
