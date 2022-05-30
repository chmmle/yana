package ru.ya.d.chmmle.yana.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ya.d.chmmle.yana.data.Note

@Database(entities = [Note::class], version = 1, exportSchema = true)
abstract class YanaDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}