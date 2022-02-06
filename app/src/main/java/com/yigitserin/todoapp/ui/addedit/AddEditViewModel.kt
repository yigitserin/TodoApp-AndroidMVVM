package com.yigitserin.todoapp.ui.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yigitserin.todoapp.R
import com.yigitserin.todoapp.data.entity.db.NoteType
import com.yigitserin.todoapp.data.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val listRepository: ListRepository
): ViewModel() {

    private val viewModelState: MutableStateFlow<AddEditViewUIState> = MutableStateFlow(AddEditViewUIState(Calendar.getInstance().time.time, null))
    internal val uiState: StateFlow<AddEditViewUIState> = viewModelState

    private val oneShotUiEventsChannel = Channel<AddEditViewUIEvent>(Channel.BUFFERED)
    internal val oneShotUiEvents: Flow<AddEditViewUIEvent> = oneShotUiEventsChannel.receiveAsFlow()

    fun setNote(isAdd: Boolean, id: Int){
        if (isAdd){
            viewModelState.value = AddEditViewUIState(Calendar.getInstance().time.time, null)
        }else{
            viewModelScope.launch {
                val note = listRepository.getNoteById(id)
                viewModelState.emit(AddEditViewUIState(note.date, note))
            }
        }
    }

    suspend fun addNote(isAdd: Boolean, id: Int, title: String, description: String?, type: NoteType){
        val date = viewModelState.value.date ?: viewModelState.value.note?.date ?: Calendar.getInstance().time.time
        val isSuccess = listRepository.saveNote(isAdd, id, title, description, type, date)
        if (isSuccess){
            oneShotUiEventsChannel.send(AddEditViewUIEvent.NavigateToList)
            oneShotUiEventsChannel.send(AddEditViewUIEvent.ShowToast(R.string.ui_addedit_save_success))
        }else{
            oneShotUiEventsChannel.send(AddEditViewUIEvent.ShowToast(R.string.ui_addedit_save_error))
        }
    }

    suspend fun deleteNote(id: Int){
        val note = listRepository.getNoteById(id)
        listRepository.deleteNote(note)
        oneShotUiEventsChannel.send(AddEditViewUIEvent.NavigateToList)
        oneShotUiEventsChannel.send(AddEditViewUIEvent.ShowToast(R.string.ui_addedit_delete_success))
    }

    fun setReminderDate(date: Long){
        viewModelScope.launch {
            viewModelState.emit(AddEditViewUIState(date, viewModelState.value.note))
        }
    }
}