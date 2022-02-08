package com.yigitserin.todoapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yigitserin.todoapp.data.repository.ListRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver @Inject constructor(): BroadcastReceiver() {
    companion object{
        const val NOTIFICATION_EXTRA_TITLE = "NOTIFICATION_EXTRA_TITLE"
        const val NOTIFICATION_EXTRA_DESCRIPTION = "NOTIFICATION_EXTRA_DESCRIPTION"
    }

    @Inject lateinit var listRepository: ListRepository
    @Inject lateinit var notificationService: NotificationService

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(NOTIFICATION_EXTRA_TITLE)
        val description = intent.getStringExtra(NOTIFICATION_EXTRA_DESCRIPTION)
        notificationService.showNotification(title ?: "", description ?: "")
    }
}