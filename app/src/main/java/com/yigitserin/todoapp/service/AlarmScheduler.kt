package com.yigitserin.todoapp.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.yigitserin.todoapp.data.entity.db.Note
import com.yigitserin.todoapp.data.entity.db.NoteType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlarmScheduler @Inject constructor(
    @ApplicationContext val context: Context
) {
    companion object{
        const val PENDING_INTENT_REQUEST_CODE = 42
    }

    fun scheduleAlarm(note: Note){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(AlarmReceiver.NOTIFICATION_EXTRA_TITLE, note.title)
        intent.putExtra(AlarmReceiver.NOTIFICATION_EXTRA_DESCRIPTION,note.description)
        val pending = PendingIntent.getBroadcast(context, PENDING_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE)
        val interval = when(note.type){
            NoteType.DAILY -> 86400000L
            NoteType.WEEKLY -> 604800000L
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, note.date, interval, pending)
    }
}