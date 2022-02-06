package com.yigitserin.todoapp.ui.addedit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yigitserin.todoapp.data.entity.db.Note
import com.yigitserin.todoapp.data.entity.db.NoteType
import com.yigitserin.todoapp.databinding.FragmentAddeditBinding
import com.yigitserin.todoapp.utils.formatToServerDateTimeDefaults
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddEditFragment @Inject constructor() : Fragment() {

    private val addEditViewModel: AddEditViewModel by viewModels()
    private lateinit var binding: FragmentAddeditBinding
    private val args: AddEditFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddeditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToOneShotEvents()
        subscribeToObservers()
        setViews()
    }

    private fun subscribeToOneShotEvents(){
        lifecycleScope.launch {
            viewLifecycleOwner.whenStarted {
                addEditViewModel.oneShotUiEvents.collectLatest { event ->
                    when (event) {
                        is AddEditViewUIEvent.NavigateToList -> findNavController().popBackStack()
                        is AddEditViewUIEvent.ShowToast -> Toast.makeText(context, getString(event.messageId), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun subscribeToObservers() {
        lifecycleScope.launch {
            addEditViewModel.uiState.collectLatest { state ->

                state.note?.let { note ->
                    initializeViewsForEdit(note)
                } ?:run{
                    binding.btnSelectDate.text = Date(state.date ?: Calendar.getInstance().time.time).formatToServerDateTimeDefaults()
                    binding.btnDelete.visibility = View.GONE
                }
            }
        }
    }

    private fun initializeViewsForEdit(note: Note){
        binding.etNoteTitle.setText(note.title)
        binding.etNoteDescription.setText(note.description)

        when(note.type){
            NoteType.DAILY -> {
                binding.rbDaily.isChecked = true
                binding.rbWeekly.isChecked = false
            }
            NoteType.WEEKLY -> {
                binding.rbDaily.isChecked = false
                binding.rbWeekly.isChecked = true
            }
        }

        binding.btnSelectDate.text = Date(note.date).formatToServerDateTimeDefaults()
        binding.btnDelete.visibility = View.VISIBLE
    }

    private fun setViews(){
        binding.btnAddEdit.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                val type = when(binding.rbDaily.isChecked){
                    true -> NoteType.DAILY
                    false -> NoteType.WEEKLY
                }
                addEditViewModel.addNote(args.isAdd, args.id, binding.etNoteTitle.text.toString(), binding.etNoteDescription.text.toString(), type )
            }
        }

        binding.btnDelete.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                addEditViewModel.deleteNote(args.id)
            }
        }

        binding.btnSelectDate.setOnClickListener {
            selectDateButtonClicked()
        }

        lifecycleScope.launchWhenCreated {
            addEditViewModel.setNote(args.isAdd, args.id)
        }
    }

    private fun selectDateButtonClicked(){
        val calendar = Calendar.getInstance()

        val selectedDate = Date(addEditViewModel.uiState.value.date ?: Calendar.getInstance().time.time)
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.time = selectedDate

        val timePickerDialog = TimePickerDialog(requireContext(),{ _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            addEditViewModel.setReminderDate(calendar.time.time)

        },selectedCalendar.get(Calendar.HOUR_OF_DAY),selectedCalendar.get(Calendar.MINUTE),true)

        DatePickerDialog(requireContext(), { _,year,month,dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            timePickerDialog.show()
        }, selectedCalendar.get(Calendar.YEAR),selectedCalendar.get(Calendar.MONTH),selectedCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }
}
