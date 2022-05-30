package ru.ya.d.chmmle.yana.screens.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.ya.d.chmmle.yana.databinding.FragmentNotesBinding

@AndroidEntryPoint
class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFab()

        val adapter = NotesAdapter {
            val action =
                NotesFragmentDirections
                    .actionNotesFragmentToNoteDetailsFragment(it.id.toString())
            findNavController().navigate(action)
        }
        binding.notesRecyclerView.adapter = adapter

        viewModel.allNotes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setupFab() {
        binding.addNote.setOnClickListener {
            val action =
                NotesFragmentDirections.actionNotesFragmentToNoteDetailsFragment(null)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}