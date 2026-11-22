package com.example.tambala.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object TambalaWorkScheduler {
    private const val WORK_NAME_REMINDER = "tambala_daily_reminder_work"
    private const val WORK_NAME_LOW_BALANCE = "tambala_low_balance_work"
    private const val WORK_NAME_BACKUP = "tambala_auto_backup_work"

    fun scheduleDailyReminder(context: Context, enabled: Boolean) {
        val workManager = WorkManager.getInstance(context)
        if (!enabled) {
            workManager.cancelUniqueWork(WORK_NAME_REMINDER)
            return
        }

        val request = PeriodicWorkRequestBuilder<DailyExpenseReminderWorker>(24, TimeUnit.HOURS)
            .setConstraints(Constraints.Builder().setRequiresBatteryNotLow(true).build())
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORK_NAME_REMINDER,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun scheduleLowBalanceMonitoring(context: Context, enabled: Boolean) {
        val workManager = WorkManager.getInstance(context)
        if (!enabled) {
            workManager.cancelUniqueWork(WORK_NAME_LOW_BALANCE)
            return
        }

        val request = PeriodicWorkRequestBuilder<LowBalanceMonitorWorker>(4, TimeUnit.HOURS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORK_NAME_LOW_BALANCE,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun scheduleAutoBackup(context: Context, enabled: Boolean, isDaily: Boolean) {
        val workManager = WorkManager.getInstance(context)
        if (!enabled) {
            workManager.cancelUniqueWork(WORK_NAME_BACKUP)
            return
        }

        val repeatInterval = if (isDaily) 24L else 7L
        val timeUnit = if (isDaily) TimeUnit.HOURS else TimeUnit.DAYS

        val request = PeriodicWorkRequestBuilder<AutoBackupWorker>(repeatInterval, timeUnit)
            .setConstraints(Constraints.Builder().setRequiresStorageNotLow(true).build())
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORK_NAME_BACKUP,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}
