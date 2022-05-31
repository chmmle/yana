package ru.ya.d.chmmle.yana.screens.notedetails

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ru.ya.d.chmmle.yana.R
import ru.ya.d.chmmle.yana.databinding.FragmentNoteDetailsBinding

@AndroidEntryPoint
class NoteDetailsFragment : Fragment() {

    private val navArgs: NoteDetailsFragmentArgs by navArgs()

    private var _binding: FragmentNoteDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteDetailsViewModel by viewModels()

    private var isDeleting = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = this@NoteDetailsFragment.viewLifecycleOwner
            noteDetailsViewModel = viewModel
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.start(navArgs.id)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_note_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menu_delete -> {
                deleteNote()
                true
            }
            else -> false
        }

    private fun deleteNote() {
        if (navArgs.id != null) {
            isDeleting = true
            viewModel.deleteNote()
            returnToNotesScreen()
        }
    }

    private fun returnToNotesScreen() {
        val action = NoteDetailsFragmentDirections.actionNoteDetailsFragmentToNotesFragment()
        findNavController().navigate(action)
    }

    private fun saveNote() {
        viewModel.saveNote()
    }

    override fun onStop() {
        super.onStop()
        if (!isDeleting) {
            saveNote()
            isDeleting = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}