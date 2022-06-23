package com.example.todolistapp.ui.Database


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.ui.Models.ToDoNoteItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<ToDoNoteItem>>
    private val repository: ToDoNoteRepository

    init {
        val userDao = ToDoDatabase.getDatabase(application).noteDao()
        repository = ToDoNoteRepository(userDao)
        readAllData = repository.readAllData
      }


    fun addNote(noteItem: ToDoNoteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNote(noteItem)
        }

    }

    fun updateNote(noteItem: ToDoNoteItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(notes = noteItem)
        }

    }

}
