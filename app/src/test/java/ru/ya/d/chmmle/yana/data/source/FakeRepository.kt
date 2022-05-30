package ru.ya.d.chmmle.yana.data.source

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.ya.d.chmmle.yana.data.Note
import java.lang.Exception

class FakeRepository: INotesRepository {
    var notesServiceData: LinkedHashMap<Int, Note> = LinkedHashMap()

    override fun getAllNotes(): Flow<List<Note>> = flow {
        emit(notesServiceData.values.toList())
    }

    override suspend fun getNote(id: Int): Note {
        notesServiceData[id]?.let {
            return it
        }
        throw Exception("Couldn't find note with id $id")
    }

    override suspend fun updateNote(note: Note): Int {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNoteById(id: Int) {
        notesServiceData.remove(id)
    }

    override suspend fun deleteAllNotes() {
        notesServiceData.clear()
    }

    override suspend fun insertNote(note: Note): Long {
        notesServiceData[note.id] = note
        return note.id.toLong()
    }

    @VisibleForTesting
    fun addNotes(vararg notes: Note) {
        for (note in notes) {
            notesServiceData[note.id] = note
        }
    }
}