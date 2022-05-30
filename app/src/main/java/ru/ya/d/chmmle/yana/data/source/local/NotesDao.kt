package ru.ya.d.chmmle.yana.data.source.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.ya.d.chmmle.yana.data.Note


@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Update
    suspend fun update(note: Note): Int

    @Delete
    suspend fun delete(note: Note)

    @Query("delete from notes where note_id = :id")
    suspend fun deleteNoteById(id: Int)

    @Query("delete from notes")
    suspend fun deleteAllNotes()

    @Query("select * from notes order by note_id desc")
    fun observeAllNotes(): Flow<List<Note>>

    @Query("select * from notes order by note_id desc")
    suspend fun getAllNotes(): List<Note>

    @Query("select * from notes where note_id = :id")
    suspend fun getNoteById(id: Int): Note
}