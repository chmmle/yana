package ru.ya.d.chmmle.yana.screens.notedetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.ya.d.chmmle.yana.MainDispatcherRule
import ru.ya.d.chmmle.yana.data.Note
import ru.ya.d.chmmle.yana.data.source.FakeRepository
import ru.ya.d.chmmle.yana.getOrAwaitValue

@ExperimentalCoroutinesApi
class NoteDetailsViewModelTest {

    private lateinit var noteDetailsViewModel: NoteDetailsViewModel
    private lateinit var notesRepository: FakeRepository
    private val note = Note(
        id = 1,
        title = "Note1",
        content = "Content1",
        dateModified = "1",
        dateCreated = "1",
        timesOpened = 1
    )

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        notesRepository = FakeRepository()
        noteDetailsViewModel = NoteDetailsViewModel(notesRepository)
    }

    @Test
    fun loadNote_noteShown() = runTest {
        // Given a note in the repository
        notesRepository.insertNote(note)

        // When loading the note with the viewModel
        noteDetailsViewModel.start(note.id.toString())
        advanceUntilIdle()

        // Then the note is loaded
        assertThat(noteDetailsViewModel.noteTitle.getOrAwaitValue(), `is`(note.title))
        assertThat(noteDetailsViewModel.noteContent.getOrAwaitValue(), `is`(note.content))
        assertThat(noteDetailsViewModel.noteDateModified.getOrAwaitValue(), `is`(note.dateModified))
        assertThat(noteDetailsViewModel.noteDateCreated.getOrAwaitValue(), `is`(note.dateCreated))
        assertThat(noteDetailsViewModel.noteTimesOpened.getOrAwaitValue(), `is`(note.timesOpened))
    }

    @Test
    fun saveNote_loadNote() = runTest {
        // Given a note loaded
        notesRepository.insertNote(note)
        noteDetailsViewModel.start(note.id.toString())

        // When saving the note
        noteDetailsViewModel.saveNote()

        // Then that note can be loaded back
        noteDetailsViewModel.start(note.id.toString())
        advanceUntilIdle()
        assertThat(noteDetailsViewModel.noteTitle.getOrAwaitValue(), `is`(note.title))
        assertThat(noteDetailsViewModel.noteContent.getOrAwaitValue(), `is`(note.content))
        assertThat(noteDetailsViewModel.noteDateModified.getOrAwaitValue(), `is`(note.dateModified))
        assertThat(noteDetailsViewModel.noteDateCreated.getOrAwaitValue(), `is`(note.dateCreated))
        assertThat(noteDetailsViewModel.noteTimesOpened.getOrAwaitValue(), `is`(note.timesOpened))
    }

    @Test
    fun newBlankNote_notSaved() {
        // Given a blank note
        noteDetailsViewModel.isNewNote = true
        noteDetailsViewModel.noteTitle.value = ""
        noteDetailsViewModel.noteContent.value = ""

        // When saving the note
        noteDetailsViewModel.saveNote()

        // Then the note is not saved
        val loaded = notesRepository.notesServiceData.values
        assertThat(loaded.isEmpty(), `is`(true))
    }
}