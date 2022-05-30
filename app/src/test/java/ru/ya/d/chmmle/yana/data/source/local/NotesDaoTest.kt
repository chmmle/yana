package ru.ya.d.chmmle.yana.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.ya.d.chmmle.yana.data.Note

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class NotesDaoTest {
    private lateinit var database: YanaDatabase

    private val note = Note(
        id = 1,
        title = "Note1",
        content = "Content1",
        dateCreated = "0",
        dateModified = "0",
        timesOpened = 0
    )

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            YanaDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun clear() {
        database.close()
    }

    @Test
    fun insertNoteAndGetById() = runTest {
        // Given - inserting a note
        database.notesDao().insert(note)

        // When - getting the note back by id
        val loaded = database.notesDao().getNoteById(note.id)

        // Then - the returned note is indeed the note we inserted earlier
        assertThat(loaded, notNullValue())
        assertThat(loaded, `is`(note))
    }

    @Test
    fun insertNoteReplacesOnConflict() = runTest {
        // Given that we inserted a note
        database.notesDao().insert(note)

        // When a note with the same id is inserted
        val newNote = note.copy(title = "Note2", content = "Content2")
        database.notesDao().insert(newNote)

        // Then the loaded note is the new note
        val loaded = database.notesDao().getNoteById(note.id)
        assertThat(loaded, `is`(newNote))
    }

    @Test
    fun insertNoteAndGetAllNotes() = runTest {
        // Given that we inserted a note
        database.notesDao().insert(note)

        // When getting all the notes from the db
        val loaded = database.notesDao().getAllNotes()

        // Then there is only 1 note in the db with the expected values
        assertThat(loaded.size, `is`(1))
        assertThat(loaded[0], `is`(note))
    }

    @Test
    fun updateNoteAndGetById() = runTest {
        // Given an inserted note
        val originalNote = note
        database.notesDao().insert(originalNote)

        // When updating the note
        val updatedNote = originalNote.copy(
            title = "Note1 New",
            content = "Content1 New",
            dateCreated = "1",
            dateModified = "1",
            timesOpened = 1
        )
        database.notesDao().update(updatedNote)

        // Then the loaded data contains the expected values
        val loaded = database.notesDao().getNoteById(originalNote.id)
        assertThat(loaded.id, `is`(originalNote.id))
        assertThat(loaded.title, `is`("Note1 New"))
        assertThat(loaded.content, `is`("Content1 New"))
        assertThat(loaded.dateCreated, `is`("1"))
        assertThat(loaded.dateModified, `is`("1"))
        assertThat(loaded.timesOpened, `is`(1))
    }

    @Test
    fun deleteNoteByIdAndGetAllNotes() = runTest {
        // Given a note inserted
        database.notesDao().insert(note)

        // When deleting a note
        database.notesDao().deleteNoteById(note.id)

        // Then the list of notes is empty
        val loaded = database.notesDao().getAllNotes()
        assertThat(loaded.isEmpty(), `is`(true))
    }

    @Test
    fun deleteAllNotesAndGetAllNotes() = runTest {
        // Given some notes inserted
        val note1 = note
        val note2 = note.copy(id = 2, title = "Note2", content = "Content2")
        val note3 = note.copy(id = 3, title = "Note3", content = "Content3")
        database.notesDao().insert(note1)
        database.notesDao().insert(note2)
        database.notesDao().insert(note3)

        // When deleting all notes
        database.notesDao().deleteAllNotes()

        // Then the list of notes is empty
        val loaded = database.notesDao().getAllNotes()
        assertThat(loaded.isEmpty(), `is`(true))
    }
}