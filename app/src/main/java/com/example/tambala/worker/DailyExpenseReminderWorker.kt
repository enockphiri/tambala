package com.example.tambala.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class DailyExpenseReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        TambalaNotificationHelper.showNotification(
            context = applicationContext,
            channelId = TambalaNotificationHelper.CHANNEL_REMINDER,
            notificationId = 1001,
            title = "Tambala Daily Reminder 💡",
            message = "Have you recorded your student upkeep expenses today? Keep your budget on track!"
        )
        return Result.success()
    }
}
