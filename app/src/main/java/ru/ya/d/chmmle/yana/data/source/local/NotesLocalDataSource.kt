package ru.ya.d.chmmle.yana.data.source.local

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.ya.d.chmmle.yana.data.Note
import ru.ya.d.chmmle.yana.data.source.INotesDataSource
import javax.inject.Inject

class NotesLocalDataSource @Inject internal constructor(
    private val notesDao: NotesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : INotesDataSource {

    override fun getAllNotes(): Flow<List<Note>> =
        notesDao.observeAllNotes()

    override suspend fun getNote(id: Int): Note = withContext(ioDispatcher) {
        notesDao.getNoteById(id)
    }

    override suspend fun updateNote(note: Note): Int = withContext(ioDispatcher) {
        notesDao.update(note)
    }

    override suspend fun deleteNoteById(id: Int) = withContext(ioDispatcher) {
        return@withContext notesDao.deleteNoteById(id)
    }

    override suspend fun deleteAllNotes() = withContext(ioDispatcher) {
        notesDao.deleteAllNotes()
    }

    override suspend fun insertNote(note: Note): Long = withContext(ioDispatcher) {
        notesDao.insert(note)
    }
}