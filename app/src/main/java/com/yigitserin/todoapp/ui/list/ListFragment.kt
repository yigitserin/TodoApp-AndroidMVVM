package com.yigitserin.todoapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yigitserin.todoapp.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment @Inject constructor(
    private val listAdapter: ListAdapter
) : Fragment() {

    private val listViewModel: ListViewModel by viewModels()
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setViews()
    }

    private fun subscribeToObservers() {
        listViewModel.notes.observe(viewLifecycleOwner){ notes ->
            listAdapter.todoItems = notes
        }
    }

    private fun setViews(){
        binding.rvTodos.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToAddEditFragment(isAdd = true, id = 0))
        }

        listAdapter.listener = ListAdapter.OnClickListener { note ->
            findNavController().navigate(ListFragmentDirections.actionListFragmentToAddEditFragment(isAdd = false, id = note.id))
        }
    }
}