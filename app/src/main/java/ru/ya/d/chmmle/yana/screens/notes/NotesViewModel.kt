package ru.ya.d.chmmle.yana.screens.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.ya.d.chmmle.yana.data.Note
import ru.ya.d.chmmle.yana.data.source.INotesRepository
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesRepository: INotesRepository
) : ViewModel() {

    val allNotes: LiveData<List<Note>> = notesRepository.getAllNotes().asLiveData()

    val empty: LiveData<Boolean> = Transformations.map(allNotes) {
        it.isEmpty()
    }
}