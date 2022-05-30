package ru.ya.d.chmmle.yana.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.ya.d.chmmle.yana.MainDispatcherRule
import ru.ya.d.chmmle.yana.data.Note

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class NotesLocalDataSourceTest {

    private lateinit var notesLocalDataSource: NotesLocalDataSource
    private lateinit var database: YanaDatabase
    private val note = Note(
        id = 1,
        title = "Note1",
        content = "Content1",
        dateModified = "1",
        dateCreated = "1",
        timesOpened = 1
    )

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            YanaDatabase::class.java
        ).allowMainThreadQueries().build()

        notesLocalDataSource = NotesLocalDataSource(database.notesDao(), Dispatchers.Main)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getAllNotes_allNotesRetrieved() = runTest {
        // Given 3 notes in the db
        val note1 = note
        val note2 = note.copy(id = 2, title = "Note2", content = "Content2")
        val note3 = note.copy(id = 3, title = "Note3", content = "Content3")
        notesLocalDataSource.insertNote(note1)
        notesLocalDataSource.insertNote(note2)
        notesLocalDataSource.insertNote(note3)

        // When retrieving all notes from the db
        val loaded = notesLocalDataSource.getAllNotes().first()

        // Then all 3 notes are retrieved
        assertThat(loaded.size, `is`(3))
    }

    @Test
    fun getNote_retrievedExpectedNote() = runTest {
        // Given a note in the db
        notesLocalDataSource.insertNote(note)

        // When retrieving the note
        notesLocalDataSource.getNote(note.id)

        // Then the note is retrieved with expected values
        val loaded = notesLocalDataSource.getNote(note.id)
        assertThat(loaded, `is`(notNullValue()))
        assertThat(loaded.id, `is`(note.id))
        assertThat(loaded.title, `is`(note.title))
        assertThat(loaded.content, `is`(note.content))
        assertThat(loaded.dateModified, `is`(note.dateModified))
        assertThat(loaded.dateCreated, `is`(note.dateCreated))
        assertThat(loaded.timesOpened, `is`(note.timesOpened))
    }

    @Test
    fun updateNote_retrieveNote() = runTest {
        // Given a note in the db
        notesLocalDataSource.insertNote(note)

        // When updating some of the note's values
        val updatedNote = note.copy(title = "Note1 Upd", content = "Content1 Upd")
        notesLocalDataSource.updateNote(updatedNote)

        // Then retrieving updated note with expected values
        val loaded = notesLocalDataSource.getNote(note.id)
        assertThat(loaded, `is`(notNullValue()))
        assertThat(loaded.id, `is`(note.id))
        assertThat(loaded.title, `is`("Note1 Upd"))
        assertThat(loaded.content, `is`("Content1 Upd"))
    }

    @Test
    fun deleteNoteById_noNoteToRetrieve() = runTest {
        // Given an inserted note
        notesLocalDataSource.insertNote(note)

        // When deleting the note
        notesLocalDataSource.deleteNoteById(note.id)

        // Then the note can't be retrieved
        val loaded = notesLocalDataSource.getNote(note.id)
        assertThat(loaded, `is`(nullValue()))
    }

    @Test
    fun deleteAllNotes_emptyListOfRetrievedNotes() = runTest {
        // Given 3 notes in the db
        val note1 = note
        val note2 = note.copy(id = 2, title = "Note2", content = "Content2")
        val note3 = note.copy(id = 3, title = "Note3", content = "Content3")
        notesLocalDataSource.insertNote(note1)
        notesLocalDataSource.insertNote(note2)
        notesLocalDataSource.insertNote(note3)

        // When deleting all notes from the db
        notesLocalDataSource.deleteAllNotes()

        // Then no notes in the db
        val loaded = notesLocalDataSource.getAllNotes().first()

        assertThat(loaded.isEmpty(), `is`(true))
    }

    @Test
    fun insertNote_getNote() = runTest {
        // Given - inserting a note
        val newNote = note
        notesLocalDataSource.insertNote(newNote)

        // When retrieving the note by id
        val loaded = notesLocalDataSource.getNote(newNote.id)

        // Then the same note returned
        assertThat(loaded, `is`(notNullValue()))
        assertThat(loaded.id, `is`(note.id))
        assertThat(loaded.title, `is`(note.title))
        assertThat(loaded.content, `is`(note.content))
        assertThat(loaded.dateCreated, `is`(note.dateCreated))
        assertThat(loaded.dateModified, `is`(note.dateModified))
        assertThat(loaded.timesOpened, `is`(note.timesOpened))
    }
}