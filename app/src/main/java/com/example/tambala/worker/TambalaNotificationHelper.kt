package com.example.tambala.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

object TambalaNotificationHelper {
    const val CHANNEL_REMINDER = "tambala_reminder_channel"
    const val CHANNEL_LOW_BALANCE = "tambala_low_balance_channel"
    const val CHANNEL_BACKUP = "tambala_backup_channel"

    fun createNotificationChannels(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val reminderChannel = NotificationChannel(
            CHANNEL_REMINDER,
            "Daily Expense Reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Reminds you to log your daily student upkeep expenses"
        }

        val lowBalanceChannel = NotificationChannel(
            CHANNEL_LOW_BALANCE,
            "Low Balance Warnings",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifies you when your semester balance reaches or falls below your set threshold"
        }

        val backupChannel = NotificationChannel(
            CHANNEL_BACKUP,
            "Automatic Local Backups",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Informs you when an automatic local backup is completed successfully"
        }

        notificationManager.createNotificationChannels(listOf(reminderChannel, lowBalanceChannel, backupChannel))
    }

    @SuppressLint("MissingPermission")
    fun showNotification(context: Context, channelId: String, notificationId: Int, title: String, message: String) {
        createNotificationChannels(context)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        try {
            notificationManager.notify(notificationId, builder.build())
        } catch (_: SecurityException) {
            // Notification permission might not be granted on Android 13+
        }
    }
}
