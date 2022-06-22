package com.example.todolistapp.ui.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ToDoNoteItem::class, ToDoNoteItemDeletedNote::class],
    version = 1, exportSchema = false )

abstract class ToDoDatabase: RoomDatabase()
{
    abstract fun noteDao(): ToDoNoteDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE:ToDoDatabase?=null

        fun getDatabase( context: Context): ToDoDatabase{
            val tempInstance= INSTANCE
            if(tempInstance!=null)
            {
                return tempInstance
            }
            synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    "note_db"
                ).build()
                INSTANCE=instance
                return instance
            }

        }
    }
}


