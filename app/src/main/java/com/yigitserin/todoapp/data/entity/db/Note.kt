package com.yigitserin.todoapp.data.entity.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var title: String,
    var description: String?,
    var type: NoteType,
    var date: Long
){
    constructor(title: String, description: String?, type: NoteType, date: Long): this(0,title, description, type, date)
}

enum class NoteType {
    DAILY, WEEKLY
}