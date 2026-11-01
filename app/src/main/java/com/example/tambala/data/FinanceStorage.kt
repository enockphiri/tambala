package com.example.tambala.data

import android.content.Context
import com.example.tambala.ui.viewmodel.BudgetData
import com.example.tambala.ui.viewmodel.TransactionData
import org.json.JSONArray
import org.json.JSONObject

class FinanceStorage(private val context: Context?) {
    private val prefs = context?.getSharedPreferences("tambala_storage", Context.MODE_PRIVATE)

    fun isInitialized(): Boolean {
        return prefs?.getBoolean("is_initialized", false) ?: false
    }

    fun setInitialized(initialized: Boolean) {
        prefs?.edit()?.putBoolean("is_initialized", initialized)?.apply()
    }

    fun saveTransactions(transactions: List<TransactionData>) {
        if (prefs == null) return
        val array = JSONArray()
        for (tx in transactions) {
            val obj = JSONObject().apply {
                put("id", tx.id)
                put("timestamp", tx.timestamp)
                put("title", tx.title)
                put("subtitle", tx.subtitle)
                put("amount", tx.amount)
                put("rawAmount", tx.rawAmount)
                put("isIncome", tx.isIncome)
                put("category", tx.category)
                put("monthTag", tx.monthTag)
            }
            array.put(obj)
        }
        prefs.edit().putString("transactions", array.toString()).apply()
    }

    fun loadTransactions(): List<JSONObject> {
        if (prefs == null) return emptyList()
        val jsonStr = prefs.getString("transactions", null) ?: return emptyList()
        val list = mutableListOf<JSONObject>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                list.add(array.getJSONObject(i))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveBudgets(budgets: List<BudgetData>) {
        if (prefs == null) return
        val array = JSONArray()
        for (b in budgets) {
            val obj = JSONObject().apply {
                put("title", b.title)
                put("rawLimit", b.rawLimit)
            }
            array.put(obj)
        }
        prefs.edit().putString("budgets", array.toString()).apply()
    }

    fun loadBudgets(): List<JSONObject> {
        if (prefs == null) return emptyList()
        val jsonStr = prefs.getString("budgets", null) ?: return emptyList()
        val list = mutableListOf<JSONObject>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                list.add(array.getJSONObject(i))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveCategories(categories: List<String>) {
        if (prefs == null) return
        val array = JSONArray()
        for (c in categories) {
            array.put(c)
        }
        prefs.edit().putString("categories", array.toString()).apply()
    }

    fun loadCategories(): List<String> {
        if (prefs == null) return emptyList()
        val jsonStr = prefs.getString("categories", null) ?: return emptyList()
        val list = mutableListOf<String>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                list.add(array.getString(i))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveNotifications(notifications: List<String>) {
        if (prefs == null) return
        val array = JSONArray()
        for (n in notifications) {
            array.put(n)
        }
        prefs.edit().putString("notifications", array.toString()).apply()
    }

    fun loadNotifications(): List<String> {
        if (prefs == null) return emptyList()
        val jsonStr = prefs.getString("notifications", null) ?: return emptyList()
        val list = mutableListOf<String>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                list.add(array.getString(i))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveSettings(
        activeSemesterNumber: Int,
        sem1StartMillis: Long,
        sem1EndMillis: Long,
        sem2StartMillis: Long,
        sem2EndMillis: Long,
        monthlyLimit: Double,
        abruptNotificationEnabled: Boolean,
        abruptThreshold: Double,
        selectedReportMonth: String,
        analysisFilter: String
    ) {
        prefs?.edit()
            ?.putInt("activeSemesterNumber", activeSemesterNumber)
            ?.putLong("sem1StartMillis", sem1StartMillis)
            ?.putLong("sem1EndMillis", sem1EndMillis)
            ?.putLong("sem2StartMillis", sem2StartMillis)
            ?.putLong("sem2EndMillis", sem2EndMillis)
            ?.putFloat("monthlyLimit", monthlyLimit.toFloat())
            ?.putBoolean("abruptNotificationEnabled", abruptNotificationEnabled)
            ?.putFloat("abruptThreshold", abruptThreshold.toFloat())
            ?.putString("selectedReportMonth", selectedReportMonth)
            ?.putString("analysisFilter", analysisFilter)
            ?.apply()
    }

    fun getInt(key: String, defaultVal: Int): Int = prefs?.getInt(key, defaultVal) ?: defaultVal
    fun getLong(key: String, defaultVal: Long): Long = prefs?.getLong(key, defaultVal) ?: defaultVal
    fun getDouble(key: String, defaultVal: Double): Double = prefs?.getFloat(key, defaultVal.toFloat())?.toDouble() ?: defaultVal
    fun getBoolean(key: String, defaultVal: Boolean): Boolean = prefs?.getBoolean(key, defaultVal) ?: defaultVal
    fun getString(key: String, defaultVal: String): String = prefs?.getString(key, defaultVal) ?: defaultVal

    fun clearAll() {
        prefs?.edit()?.clear()?.apply()
    }
}
