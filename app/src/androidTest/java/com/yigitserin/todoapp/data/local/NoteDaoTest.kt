package com.yigitserin.todoapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.yigitserin.todoapp.data.entity.db.Note
import com.yigitserin.todoapp.data.entity.db.NoteType
import com.yigitserin.todoapp.util.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class NoteDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: NoteDatabase
    private lateinit var dao: NoteDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.noteDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertNote() = runBlocking{
        val noteTitle = "TestNoteTitle"
        val noteDescription = "TestNoteDescription"
        val noteType = NoteType.DAILY
        val noteDate = Calendar.getInstance().time.time

        val note = Note(noteTitle, noteDescription, noteType, noteDate)
        dao.insertNote(note)

        val allNotes = dao.observeAllNotes().getOrAwaitValue()
        val containsNote = allNotes.firstOrNull {
            ((it.title == noteTitle) && (it.description == noteDescription) && (it.type == noteType) && (it.date == noteDate))
        } != null
        assert(containsNote)
    }

    @Test
    fun deleteNote() = runBlocking {
        val noteTitle = "TestNoteTitle"
        val noteDescription = "TestNoteDescription"
        val noteType = NoteType.DAILY
        val noteDate = Calendar.getInstance().time.time

        val note = Note(noteTitle, noteDescription, noteType, noteDate)
        dao.insertNote(note)
        dao.deleteNote(note)

        val allNotes = dao.observeAllNotes().getOrAwaitValue()
        val containsNote = allNotes.firstOrNull {
            ((it.title == noteTitle) && (it.description == noteDescription) && (it.type == noteType) && (it.date == noteDate))
        } != null
        assert(containsNote)
    }
}
