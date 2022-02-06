package com.yigitserin.todoapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yigitserin.todoapp.data.entity.db.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes_table")
    fun observeAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_table WHERE id=:id ")
    fun getNoteById(id: Int): Note
}