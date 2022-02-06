package com.yigitserin.todoapp.data.repository

import androidx.lifecycle.LiveData
import com.yigitserin.todoapp.data.entity.db.Note
import com.yigitserin.todoapp.data.entity.db.NoteType
import com.yigitserin.todoapp.data.local.NoteDao
import com.yigitserin.todoapp.utils.Validator
import javax.inject.Inject

class DefaultListRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val validator: Validator,
): ListRepository {

    override suspend fun saveNote(isAdd: Boolean, id: Int, title: String, description: String?, type: NoteType, date: Long): Boolean {
        return if (isAdd){
            createNote(title, description, type, date)
        }else{
            editNote(id, title, description, type, date)
        }
    }

    private suspend fun createNote(title: String, description: String?, type: NoteType, date: Long): Boolean{
        if (!validator.validateTextNotEmpty(title)){
            return false
        }

        val note = Note(title, description, type, date)
        noteDao.insertNote(note)
        return true
    }

    private suspend fun editNote(id: Int, title: String, description: String?, type: NoteType, date: Long): Boolean{
        if (!validator.validateTextNotEmpty(title)){
            return false
        }

        val note = getNoteById(id)
        note.title = title
        note.description = description
        note.type = type
        note.date = date
        noteDao.updateNote(note)
        return true
    }

    override fun getNoteById(id: Int): Note {
        return noteDao.getNoteById(id)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    override fun observeAllNotes(): LiveData<List<Note>> {
        return noteDao.observeAllNotes()
    }
}