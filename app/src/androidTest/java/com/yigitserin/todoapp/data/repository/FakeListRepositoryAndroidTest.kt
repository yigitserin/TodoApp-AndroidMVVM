package com.yigitserin.todoapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yigitserin.todoapp.data.entity.db.Note
import com.yigitserin.todoapp.data.entity.db.NoteType
import com.yigitserin.todoapp.utils.Validator

class FakeListRepositoryAndroidTest: ListRepository {

    private val notes = mutableListOf<Note>()
    private val observableNotes = MutableLiveData<List<Note>>(notes)

    private val validator = Validator()

    override suspend fun saveNote(isAdd: Boolean, id: Int, title: String, description: String?, type: NoteType, date: Long): Boolean {

        if (!validator.validateTextNotEmpty(title)){
            return false
        }

        if (isAdd){
            val note = Note(title, description, type, date)
            notes.add(note)
        }else{
            val editNote = notes.first { it.id == id }
            editNote.title = title
            editNote.description = description
            editNote.type = type
            editNote.date = date
        }
        return true
    }

    override fun getNoteById(id: Int): Note {
        return notes.first { it.id == id }
    }

    override suspend fun deleteNote(note: Note) {
        notes.remove(note)
    }

    override fun observeAllNotes(): LiveData<List<Note>> {
        return observableNotes
    }
}