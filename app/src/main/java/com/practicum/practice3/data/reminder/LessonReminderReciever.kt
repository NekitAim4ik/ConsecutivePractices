package com.practicum.practice3.data.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class LessonReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val ownerName = intent.getStringExtra(EXTRA_OWNER_NAME).orEmpty()
        ReminderNotifications.showLessonStarted(context, ownerName)
    }

    companion object {
        const val EXTRA_OWNER_NAME = "owner_name"
    }
}