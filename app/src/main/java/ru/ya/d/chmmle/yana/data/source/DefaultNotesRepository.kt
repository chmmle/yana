package ru.ya.d.chmmle.yana.data.source

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.ya.d.chmmle.yana.data.Note
import javax.inject.Inject

class DefaultNotesRepository @Inject constructor(
    private val notesLocalDataSource: INotesDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) : INotesRepository {

    override fun getAllNotes(): Flow<List<Note>> =
        notesLocalDataSource.getAllNotes()

    override suspend fun getNote(id: Int): Note =
        notesLocalDataSource.getNote(id)

    override suspend fun updateNote(note: Note): Int = withContext(ioDispatcher) {
        notesLocalDataSource.updateNote(note)
    }

    override suspend fun deleteNoteById(id: Int) = withContext(ioDispatcher) {
        notesLocalDataSource.deleteNoteById(id)
    }

    override suspend fun deleteAllNotes() = withContext(ioDispatcher) {
        notesLocalDataSource.deleteAllNotes()
    }

    override suspend fun insertNote(note: Note): Long = withContext(ioDispatcher) {
        notesLocalDataSource.insertNote(note)
    }
}