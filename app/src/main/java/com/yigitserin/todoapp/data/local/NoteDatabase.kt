package com.yigitserin.todoapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.yigitserin.todoapp.data.entity.db.Note


@Database(entities = [Note::class], version = 2)
abstract class NoteDatabase : RoomDatabase() {

    companion object{
        const val DB_NAME = "note_db"

        val MIGRATION_1_2 = Migration(1,2) {
            it.execSQL("ALTER TABLE notes_table ADD COLUMN date LONG")
        }
    }

    abstract fun noteDao(): NoteDao
}