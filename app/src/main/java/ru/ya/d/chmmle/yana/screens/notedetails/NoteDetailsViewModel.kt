package ru.ya.d.chmmle.yana.screens.notedetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ya.d.chmmle.yana.data.Note
import ru.ya.d.chmmle.yana.data.source.INotesRepository
import javax.inject.Inject


private const val EMPTY_STRING = ""

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(private val notesRepository: INotesRepository) :
    ViewModel() {

    var isNewNote: Boolean = false

    val noteTitle = MutableLiveData<String>()
    val noteContent = MutableLiveData<String>()
    val noteDateCreated = MutableLiveData<String>()
    val noteDateModified = MutableLiveData<String>()
    val noteTimesOpened = MutableLiveData<Long>()

    private var noteId: String? = null

    private var isDataLoaded: Boolean = false

    fun start(noteId: String?) {
        this.noteId = noteId
        if (noteId == null) {
            isNewNote = true
            return
        }

        if (isDataLoaded) {
            return
        }

        isNewNote = false

        viewModelScope.launch {
            val note = notesRepository.getNote(noteId.toInt())
            onNoteLoaded(note)
        }
    }

    private fun onNoteLoaded(note: Note) {
        noteTitle.value = note.title
        noteContent.value = note.content
        noteDateCreated.value = note.dateCreated
        noteDateModified.value = note.dateModified
        noteTimesOpened.value = note.timesOpened
        isDataLoaded = true
    }

    fun saveNote() {
        if (isNewNote &&
            noteTitle.value.isNullOrEmpty() &&
            noteContent.value.isNullOrEmpty()
        ) {
            return
        }

        viewModelScope.launch {
            var note = Note(
                title = noteTitle.value ?: EMPTY_STRING,
                content = noteContent.value ?: EMPTY_STRING,
                dateCreated = noteDateCreated.value ?: EMPTY_STRING,
                dateModified = noteDateModified.value ?: EMPTY_STRING,
                timesOpened = noteTimesOpened.value ?: 0
            )
            if (!isNewNote) {
                note = note.copy(id = noteId!!.toInt())
            }
            notesRepository.insertNote(note)
        }
    }

    fun deleteNote() {
        noteId?.let {
            viewModelScope.launch {
                notesRepository.deleteNoteById(it.toInt())
            }
        }
    }
}