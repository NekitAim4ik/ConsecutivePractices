package com.practicum.practice3.data.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.practicum.practice3.domain.model.LessonTime
import com.practicum.practice3.domain.repository.ReminderScheduler
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class AlarmReminderScheduler(context: Context) : ReminderScheduler {

    private val appContext = context.applicationContext
    private val alarmManager = appContext.getSystemService(AlarmManager::class.java)

    override fun schedule(time: LessonTime, ownerName: String) {
        val intent = Intent(appContext, LessonReminderReceiver::class.java)
            .putExtra(LessonReminderReceiver.EXTRA_OWNER_NAME, ownerName)

        // Один и тот же requestCode: новый вызов заменяет прошлое напоминание.
        val operation = PendingIntent.getBroadcast(
            appContext,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerAtMillis = nextTriggerMillis(time)

        try {
            if (canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    operation
                )
            } else {
                // Нет права на точные будильники: напоминание может прийти с небольшой задержкой.
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    operation
                )
            }
        } catch (e: SecurityException) {
            // Право могли отозвать между проверкой и вызовом: ставим неточный будильник.
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, operation)
        }
    }

    private fun canScheduleExactAlarms(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()

    private fun nextTriggerMillis(time: LessonTime): Long {
        val zone = ZoneId.systemDefault()
        var trigger = LocalDate.now(zone).atTime(time.hour, time.minute).atZone(zone)
        if (!trigger.isAfter(ZonedDateTime.now(zone))) {
            trigger = trigger.plusDays(1)
        }
        return trigger.toInstant().toEpochMilli()
    }

    private companion object {
        const val REQUEST_CODE = 7001
    }
}