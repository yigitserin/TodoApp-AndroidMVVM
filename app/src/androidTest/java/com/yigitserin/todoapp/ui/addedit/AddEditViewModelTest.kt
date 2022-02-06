package com.yigitserin.todoapp.ui.addedit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yigitserin.todoapp.MainCoroutineRule
import com.yigitserin.todoapp.R
import com.yigitserin.todoapp.data.entity.db.NoteType
import com.yigitserin.todoapp.data.repository.FakeListRepositoryAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: AddEditViewModel

    @Before
    fun setup() {
        viewModel = AddEditViewModel(FakeListRepositoryAndroidTest())
    }

    @Test
    fun insertEmptyTitleNote() = runBlocking{
        viewModel.addNote(isAdd = true, 0, "", "Description", NoteType.DAILY)
        viewModel.oneShotUiEvents.collect {
            assert(it == AddEditViewUIEvent.ShowToast(R.string.ui_addedit_save_error))
        }
    }
}