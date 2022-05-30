package ru.ya.d.chmmle.yana.data.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.ya.d.chmmle.yana.MainDispatcherRule
import ru.ya.d.chmmle.yana.data.Note

@ExperimentalCoroutinesApi
class DefaultNotesRepositoryTest {

    private lateinit var notesRepository: DefaultNotesRepository
    private lateinit var notesLocalDataSource: FakeDataSource

    private val note1 = Note(1, "Note1", "Content1", "1", "1", 1)
    private val note2 = Note(2, "Note2", "Content2", "2", "2", 2)
    private val note3 = Note(3, "Note3", "Content3", "3", "3", 3)
    private val localNotes = listOf(note1, note2).sortedBy { it.id }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        notesLocalDataSource = FakeDataSource(localNotes.toMutableList())
        notesRepository = DefaultNotesRepository(notesLocalDataSource, Dispatchers.Main)
    }

    @Test
    fun getAllNotes_allNotesRetrieved() = runTest {
        // Given 2 notes in the repository

        // When trying to retrieve all of the notes
        val loaded = notesRepository.getAllNotes().first()

        // Then all notes are retrieved
        assertThat(loaded.size, `is`(2))
    }

    @Test
    fun getNote_correctNoteRetrieved() = runTest {
        // Given 2 notes in the repository

        // When retrieving a note
        val loaded = notesRepository.getNote(note1.id)

        // Then the correct note is retrieved
        assertThat(loaded, `is`(notNullValue()))
        assertThat(loaded.id, `is`(note1.id))
        assertThat(loaded.title, `is`(note1.title))
        assertThat(loaded.content, `is`(note1.content))
        assertThat(loaded.dateModified, `is`(note1.dateModified))
        assertThat(loaded.dateCreated, `is`(note1.dateCreated))
        assertThat(loaded.timesOpened, `is`(note1.timesOpened))
    }

    @Test
    fun updateNote_updatedNoteRetrieved() = runTest {
        // Given an updated note
        val updatedNote = note2

        // When updating the note
        notesRepository.updateNote(updatedNote)

        // Then an updated version of the note can be retrieved
        val loaded = notesRepository.getNote(updatedNote.id)
        assertThat(loaded, `is`(notNullValue()))
        assertThat(loaded.id, `is`(updatedNote.id))
        assertThat(loaded.title, `is`(updatedNote.title))
        assertThat(loaded.content, `is`(updatedNote.content))
        assertThat(loaded.dateModified, `is`(updatedNote.dateModified))
        assertThat(loaded.dateCreated, `is`(updatedNote.dateCreated))
        assertThat(loaded.timesOpened, `is`(updatedNote.timesOpened))
    }

    @Test
    fun deleteNoteById_unableToRetrieveNote() = runTest {
        // Given two notes in the repository

        // When deleting one of the notes by id
        notesRepository.deleteNoteById(note2.id)

        // Then only one note is in the repository
        val loaded = notesRepository.getAllNotes().first()
        assertThat(loaded.size, `is`(1))
        assertThat(loaded.contains(note1), `is`(true))
        assertThat(loaded.contains(note2), `is`(false))
    }

    @Test
    fun deleteAllNotes_emptyRepository() = runTest {
        // Given 2 notes in the data source

        // When deleting all notes
        notesRepository.deleteAllNotes()

        // Then no notes to retrieve
        val loaded = notesRepository.getAllNotes().first()
        assertThat(loaded.isEmpty(), `is`(true))
    }

    @Test
    fun insertNote_retrieveNote() = runTest {
        // Given a note to insert
        val note = note3

        // When inserting the note
        notesRepository.insertNote(note)

        // Then the note can be retrieved with expected values
        val loaded = notesRepository.getNote(note.id)
        assertThat(loaded, `is`(notNullValue()))
        assertThat(loaded.id, `is`(note.id))
        assertThat(loaded.title, `is`(note.title))
        assertThat(loaded.content, `is`(note.content))
        assertThat(loaded.dateModified, `is`(note.dateModified))
        assertThat(loaded.dateCreated, `is`(note.dateCreated))
        assertThat(loaded.timesOpened, `is`(note.timesOpened))
    }
}