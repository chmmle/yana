@file:Suppress("MemberVisibilityCanBePrivate")

package ru.ya.d.chmmle.yana.data.source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.ya.d.chmmle.yana.data.Note

class FakeDataSource(var notes: MutableList<Note>? = mutableListOf()) : INotesDataSource {
    override fun getAllNotes(): Flow<List<Note>> {
        return flow {
            emit(notes!!)
        }
    }

    override suspend fun getNote(id: Int): Note {
        notes?.let {
            for (note in notes!!) {
                if (note.id == id) {
                    return note
                }
            }
        }
        throw Exception("Couldn't find note with id $id")
    }

    override suspend fun updateNote(note: Note): Int {
        notes?.apply {
            removeIf { it.id == note.id }
            add(note)
            return 1
        }
        return 0
    }

    override suspend fun deleteNoteById(id: Int) {
        notes?.removeIf { it.id == id }
    }

    override suspend fun deleteAllNotes() {
        notes?.clear()
    }

    override suspend fun insertNote(note: Note): Long {
        return notes?.let {
            it.add(note)
            note.id.toLong()
        } ?: throw Exception("Couldn't insert note")
    }
}