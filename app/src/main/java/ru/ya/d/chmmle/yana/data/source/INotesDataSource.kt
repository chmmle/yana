package ru.ya.d.chmmle.yana.data.source

import kotlinx.coroutines.flow.Flow
import ru.ya.d.chmmle.yana.data.Note

interface INotesDataSource {

    fun getAllNotes(): Flow<List<Note>>

    suspend fun getNote(id: Int): Note

    suspend fun updateNote(note: Note): Int

    suspend fun deleteNoteById(id: Int)

    suspend fun deleteAllNotes()

    suspend fun insertNote(note: Note): Long
}