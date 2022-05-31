package ru.ya.d.chmmle.yana.screens.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.ya.d.chmmle.yana.R
import ru.ya.d.chmmle.yana.data.Note
import ru.ya.d.chmmle.yana.data.contentShort
import ru.ya.d.chmmle.yana.data.titleShort


class NotesAdapter(private val onClick: (Note) -> Unit) :
    ListAdapter<Note, NotesAdapter.NotesViewHolder>(diffCallback) {

    class NotesViewHolder(item: View, val onClick: (Note) -> Unit) : RecyclerView.ViewHolder(item) {
        private val titleTextView: TextView = item.findViewById(R.id.note_title)
        private val contentTextView: TextView = item.findViewById(R.id.note_content)
        private var currentNote: Note? = null

        init {
            item.setOnClickListener {
                currentNote?.let { note ->
                    onClick(note)
                }
            }
        }

        fun bind(note: Note) {
            currentNote = note
            titleTextView.text = note.title
            contentTextView.text = note.contentShort()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NotesViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }
    }
}