package com.example.tambala.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class LowBalanceMonitorWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val prefs = applicationContext.getSharedPreferences("tambala_prefs", Context.MODE_PRIVATE)
        val isEnabled = prefs.getBoolean("low_balance_enabled", true)
        val threshold = prefs.getFloat("low_balance_threshold", 10000f).toDouble()
        val currentBalance = prefs.getFloat("current_balance_cache", 354200f).toDouble()

        if (!isEnabled) return Result.success()

        val wasBelowThreshold = prefs.getBoolean("was_below_threshold", false)

        if (currentBalance <= threshold) {
            if (!wasBelowThreshold) {
                TambalaNotificationHelper.showNotification(
                    context = applicationContext,
                    channelId = TambalaNotificationHelper.CHANNEL_LOW_BALANCE,
                    notificationId = 1002,
                    title = "⚠️ Low Balance Warning",
                    message = "Your remaining semester balance (MK %,d) has reached or fallen below your threshold (MK %,d). Spend wisely!".format(currentBalance.toInt(), threshold.toInt())
                )
                prefs.edit().putBoolean("was_below_threshold", true).apply()
            }
        } else {
            if (wasBelowThreshold) {
                prefs.edit().putBoolean("was_below_threshold", false).apply()
            }
        }

        return Result.success()
    }
}
