package ru.ya.d.chmmle.yana.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "date_modified")
    val dateModified: String,

    @ColumnInfo(name = "date_created")
    val dateCreated: String,

    @ColumnInfo(name = "times_opened")
    val timesOpened: Long
)

fun Note.contentShort(): String {
    return if (content.length < 200) {
        content
    } else {
        content.take(200) + "..."
    }
}

