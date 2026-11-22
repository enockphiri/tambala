package com.example.tambala.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AutoBackupWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val prefs = applicationContext.getSharedPreferences("tambala_prefs", Context.MODE_PRIVATE)
        val isEnabled = prefs.getBoolean("auto_backup_enabled", true)

        if (!isEnabled) return Result.success()

        try {
            val backupDir = File(applicationContext.filesDir, "backups")
            if (!backupDir.exists()) {
                backupDir.mkdirs()
            }

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val backupFile = File(backupDir, "tambala_backup_$timestamp.json")

            val balance = prefs.getString("balance_cache", "MK 354,200") ?: "MK 354,200"
            val income = prefs.getString("income_cache", "+MK 450,000") ?: "+MK 450,000"
            val expenses = prefs.getString("expenses_cache", "-MK 95,800") ?: "-MK 95,800"

            val jsonContent = """
                {
                    "app": "Tambala",
                    "timestamp": "$timestamp",
                    "balance": "$balance",
                    "total_income": "$income",
                    "total_expenses": "$expenses"
                }
            """.trimIndent()

            backupFile.writeText(jsonContent)

            TambalaNotificationHelper.showNotification(
                context = applicationContext,
                channelId = TambalaNotificationHelper.CHANNEL_BACKUP,
                notificationId = 1003,
                title = "💾 Automatic Local Backup Saved",
                message = "Your financial data was successfully backed up locally to ${backupFile.name}."
            )

            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }
    }
}
