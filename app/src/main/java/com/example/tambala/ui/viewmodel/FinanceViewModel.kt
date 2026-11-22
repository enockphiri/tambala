package com.example.tambala.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Work
import java.text.SimpleDateFormat
import java.util.Locale

data class TransactionData(
    val id: Long = System.currentTimeMillis(),
    val timestamp: Long = System.currentTimeMillis(),
    val title: String,
    val subtitle: String,
    val amount: String,
    val rawAmount: Double,
    val isIncome: Boolean,
    val category: String,
    val monthTag: String, // e.g. "October", "November"
    val icon: ImageVector
)

data class BudgetData(
    val title: String,
    var spent: String,
    var rawSpent: Double,
    var limit: String,
    var rawLimit: Double,
    var remaining: String,
    var progress: Float,
    val targetPerDay: String,
    val icon: ImageVector
)

data class ReportItemData(
    val title: String,
    val amount: String,
    val percentage: String
)

class FinanceViewModel : ViewModel() {
    var balance by mutableStateOf("MK 0")
    var totalIncome by mutableStateOf("+MK 0")
    var totalExpenses by mutableStateOf("-MK 0")

    var rawBalance = 0.0
    var rawIncome = 0.0
    var rawExpenses = 0.0

    // Dynamic Semester configuration (1 or 2, without hardcoded years)
    var activeSemesterNumber by mutableStateOf(1) // 1 or 2
    
    val semesterName: String
        get() = "Semester $activeSemesterNumber"

    var sem1StartMillis by mutableStateOf(0L)
    var sem1EndMillis by mutableStateOf(0L)

    var sem2StartMillis by mutableStateOf(0L)
    var sem2EndMillis by mutableStateOf(0L)

    var daysLeft by mutableIntStateOf(0)

    val safeDailySpending: Double
        get() {
            val days = daysLeft.coerceAtLeast(1)
            return rawBalance / days
        }

    var monthlyLimit by mutableStateOf(0.0)
    var abruptNotificationEnabled by mutableStateOf(true)
    var abruptThreshold by mutableStateOf(10000.0)

    fun updateMonthlyLimit(limitStr: String): Boolean {
        val limitVal = limitStr.toDoubleOrNull()
        if (limitVal == null || limitVal <= 0.0) {
            validationError = "Enter an allocation greater than 0"
            return false
        }
        validationError = null
        monthlyLimit = limitVal
        return true
    }

    fun recalculateTotals() {
        rawIncome = transactions.filter { it.isIncome }.sumOf { it.rawAmount }
        rawExpenses = transactions.filter { !it.isIncome }.sumOf { it.rawAmount }
        rawBalance = rawIncome - rawExpenses
        totalIncome = "+MK %,d".format(rawIncome.toInt())
        totalExpenses = "-MK %,d".format(rawExpenses.toInt())
        balance = "MK %,d".format(rawBalance.toInt())
    }

    // Analysis filter state ("Day", "Week", "Month", "Semester")
    var analysisFilter by mutableStateOf("Semester")

    // Jan-Dec available months for past & present reporting
    val availableMonths = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    var selectedReportMonth by mutableStateOf("November")

    // Convenience methods for UI references
    fun updateAnalysisFilter(filter: String) {
        analysisFilter = filter
    }

    fun updateSelectedReportMonth(month: String) {
        selectedReportMonth = month
    }

    fun deleteBudget(title: String) {
        budgets.removeAll { it.title.equals(title, ignoreCase = true) }
    }

    val totalCategoryBudgetsRaw: Double
        get() = budgets.sumOf { rawLimit -> rawLimit.rawLimit }

    // Error state for validation matching Test Cases
    var validationError by mutableStateOf<String?>(null)

    // Unread notification state for TopNavBar bell badge
    var hasUnreadNotifications by mutableStateOf(true)

    // Notifications history list for Abrupt Spending and System alerts
    val notificationsList = mutableStateListOf(
        "🔔 Welcome to Tambala! 100% offline student upkeep tracker is active.",
        "💡 Tip: Create your custom categories and add budget limits to get started."
    )

    // User enters their own data from scratch (empty initial lists)
    val transactions = mutableStateListOf<TransactionData>()
    val budgets = mutableStateListOf<BudgetData>()
    val categories = mutableStateListOf<String>()

    var abruptAlertMessage by mutableStateOf<String?>(null)

    fun cacheFinancialState(context: Context) {
        val prefs = context.getSharedPreferences("tambala_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("balance_cache", balance)
            .putString("income_cache", totalIncome)
            .putString("expenses_cache", totalExpenses)
            .putFloat("current_balance_cache", rawBalance.toFloat())
            .apply()
    }

    fun addNotification(msg: String) {
        notificationsList.add(0, msg)
        hasUnreadNotifications = true
    }

    private fun formatDate(millis: Long): String {
        if (millis == 0L) return "Not set (Tap to configure)"
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return formatter.format(millis)
    }

    val activeStartStr: String
        get() = formatDate(if (activeSemesterNumber == 1) sem1StartMillis else sem2StartMillis)

    val activeEndStr: String
        get() = formatDate(if (activeSemesterNumber == 1) sem1EndMillis else sem2EndMillis)

    fun updateStartDate(millis: Long) {
        if (activeSemesterNumber == 1) {
            sem1StartMillis = millis
        } else {
            sem2StartMillis = millis
        }
        recalculateDaysLeft()
    }

    fun updateEndDate(millis: Long) {
        if (millis < System.currentTimeMillis()) {
            validationError = "End date cannot be in the past. Please choose a future date."
            return
        }
        if (activeSemesterNumber == 1) {
            sem1EndMillis = millis
        } else {
            sem2EndMillis = millis
        }
        recalculateDaysLeft()
    }

    private fun recalculateDaysLeft() {
        val end = if (activeSemesterNumber == 1) sem1EndMillis else sem2EndMillis
        if (end == 0L) {
            daysLeft = 0
            return
        }
        val diff = end - System.currentTimeMillis()
        val calculated = (diff / (1000 * 60 * 60 * 24)).toInt()
        daysLeft = calculated.coerceAtLeast(0)
    }

    fun addTransaction(title: String, amountStr: String, isIncome: Boolean, category: String, dateMillis: Long, dateStr: String): Boolean {
        if (amountStr.isBlank()) {
            validationError = "Amount is required"
            return false
        }
        val amountVal = amountStr.toDoubleOrNull()
        if (amountVal == null) {
            validationError = "Enter a valid amount"
            return false
        }
        if (amountVal <= 0.0) {
            validationError = "Enter an amount greater than 0"
            return false
        }

        validationError = null
        val formattedAmount = if (isIncome) "+MK %,d".format(amountVal.toInt()) else "-MK %,d".format(amountVal.toInt())
        val finalCategory = if (isIncome) "Income Source" else category
        
        // Proper distinct income vs expense icons
        val icon = if (isIncome) {
            Icons.AutoMirrored.Filled.TrendingUp
        } else {
            when {
                finalCategory.lowercase().contains("food") || finalCategory.lowercase().contains("dining") -> Icons.Default.Restaurant
                finalCategory.lowercase().contains("transport") -> Icons.Default.DirectionsBus
                finalCategory.lowercase().contains("print") -> Icons.Default.Print
                finalCategory.lowercase().contains("wifi") || finalCategory.lowercase().contains("data") -> Icons.Default.Wifi
                finalCategory.lowercase().contains("hostel") || finalCategory.lowercase().contains("rent") -> Icons.Default.Home
                finalCategory.lowercase().contains("project") -> Icons.Default.Work
                finalCategory.lowercase().contains("emergency") -> Icons.Default.LocalHospital
                else -> Icons.Default.Category
            }
        }

        val subtitleStr = "$dateStr • $finalCategory"
        transactions.add(0, TransactionData(System.currentTimeMillis(), dateMillis, title.ifBlank { finalCategory }, subtitleStr, formattedAmount, amountVal, isIncome, finalCategory, selectedReportMonth, icon))

        if (isIncome) {
            rawIncome += amountVal
            rawBalance += amountVal
        } else {
            rawExpenses += amountVal
            rawBalance -= amountVal

            if (abruptNotificationEnabled && amountVal >= abruptThreshold) {
                val alertMsg = "⚠️ Abrupt Spending Warning: '${title.ifBlank { finalCategory }}' cost MK %,d (exceeds threshold of MK %,d)!".format(amountVal.toInt(), abruptThreshold.toInt())
                abruptAlertMessage = alertMsg
                addNotification(alertMsg)
            }

            val budget = budgets.find { it.title.equals(finalCategory, ignoreCase = true) }
            if (budget != null) {
                budget.rawSpent += amountVal
                budget.spent = "MK %,d".format(budget.rawSpent.toInt())
                val rem = budget.rawLimit - budget.rawSpent
                budget.remaining = if (rem >= 0) "MK %,d left".format(rem.toInt()) else "MK %,d over".format((-rem).toInt())
                budget.progress = ((budget.rawSpent / budget.rawLimit).toFloat()).coerceIn(0f, 1f)
            }
        }

        totalIncome = "+MK %,d".format(rawIncome.toInt())
        totalExpenses = "-MK %,d".format(rawExpenses.toInt())
        balance = "MK %,d".format(rawBalance.toInt())
        return true
    }

    fun editTransaction(id: Long, newTitle: String, newAmountStr: String): Boolean {
        if (newAmountStr.isBlank()) {
            validationError = "Amount is required"
            return false
        }
        val newAmt = newAmountStr.toDoubleOrNull()
        if (newAmt == null || newAmt <= 0.0) {
            validationError = "Enter an amount greater than 0"
            return false
        }

        validationError = null
        val tx = transactions.find { it.id == id }
        if (tx != null) {
            if (tx.isIncome) {
                rawIncome -= tx.rawAmount
                rawBalance -= tx.rawAmount
            } else {
                rawExpenses -= tx.rawAmount
                rawBalance += tx.rawAmount

                val budget = budgets.find { it.title.equals(tx.category, ignoreCase = true) }
                if (budget != null) {
                    budget.rawSpent = (budget.rawSpent - tx.rawAmount).coerceAtLeast(0.0)
                    budget.spent = "MK %,d".format(budget.rawSpent.toInt())
                    val rem = budget.rawLimit - budget.rawSpent
                    budget.remaining = if (rem >= 0) "MK %,d left".format(rem.toInt()) else "MK %,d over".format((-rem).toInt())
                    budget.progress = if (budget.rawLimit > 0) ((budget.rawSpent / budget.rawLimit).toFloat()).coerceIn(0f, 1f) else 0f
                }
            }

            val formattedAmount = if (tx.isIncome) "+MK %,d".format(newAmt.toInt()) else "-MK %,d".format(newAmt.toInt())
            val updatedTx = tx.copy(title = newTitle.ifBlank { tx.title }, rawAmount = newAmt, amount = formattedAmount)
            val index = transactions.indexOf(tx)
            transactions[index] = updatedTx

            if (tx.isIncome) {
                rawIncome += newAmt
                rawBalance += newAmt
            } else {
                rawExpenses += newAmt
                rawBalance -= newAmt

                if (abruptNotificationEnabled && newAmt >= abruptThreshold) {
                    val alertMsg = "⚠️ Abrupt Spending Warning: '${updatedTx.title}' updated to MK %,d (exceeds threshold)!".format(newAmt.toInt())
                    abruptAlertMessage = alertMsg
                    addNotification(alertMsg)
                }

                val budget = budgets.find { it.title.equals(tx.category, ignoreCase = true) }
                if (budget != null) {
                    budget.rawSpent += newAmt
                    budget.spent = "MK %,d".format(budget.rawSpent.toInt())
                    val rem = budget.rawLimit - budget.rawSpent
                    budget.remaining = if (rem >= 0) "MK %,d left".format(rem.toInt()) else "MK %,d over".format((-rem).toInt())
                    budget.progress = ((budget.rawSpent / budget.rawLimit).toFloat()).coerceIn(0f, 1f)
                }
            }

            totalIncome = "+MK %,d".format(rawIncome.toInt())
            totalExpenses = "-MK %,d".format(rawExpenses.toInt())
            balance = "MK %,d".format(rawBalance.toInt())
            return true
        }
        return false
    }

    fun deleteTransaction(id: Long) {
        val tx = transactions.find { it.id == id }
        if (tx != null) {
            if (tx.isIncome) {
                rawIncome = (rawIncome - tx.rawAmount).coerceAtLeast(0.0)
                rawBalance = rawBalance - tx.rawAmount
            } else {
                rawExpenses = (rawExpenses - tx.rawAmount).coerceAtLeast(0.0)
                rawBalance = rawBalance + tx.rawAmount

                val budget = budgets.find { it.title.equals(tx.category, ignoreCase = true) }
                if (budget != null) {
                    budget.rawSpent = (budget.rawSpent - tx.rawAmount).coerceAtLeast(0.0)
                    budget.spent = "MK %,d".format(budget.rawSpent.toInt())
                    val rem = budget.rawLimit - budget.rawSpent
                    budget.remaining = if (rem >= 0) "MK %,d left".format(rem.toInt()) else "MK %,d over".format((-rem).toInt())
                    budget.progress = if (budget.rawLimit > 0) ((budget.rawSpent / budget.rawLimit).toFloat()).coerceIn(0f, 1f) else 0f
                }
            }
            totalIncome = "+MK %,d".format(rawIncome.toInt())
            totalExpenses = "-MK %,d".format(rawExpenses.toInt())
            balance = "MK %,d".format(rawBalance.toInt())
            transactions.remove(tx)
        }
    }

    fun addCategory(categoryName: String): Boolean {
        if (categoryName.isBlank()) {
            validationError = "Category name cannot be empty"
            return false
        }
        if (categories.contains(categoryName)) {
            validationError = "Category already exists"
            return false
        }
        validationError = null
        categories.add(categoryName)
        return true
    }

    fun editCategory(oldName: String, newName: String): Boolean {
        if (newName.isBlank()) {
            validationError = "Category name cannot be empty"
            return false
        }
        val index = categories.indexOf(oldName)
        if (index != -1) {
            validationError = null
            categories[index] = newName
            val budget = budgets.find { it.title.equals(oldName, ignoreCase = true) }
            if (budget != null) {
                val idx = budgets.indexOf(budget)
                budgets[idx] = budget.copy(title = newName)
            }
            return true
        }
        return false
    }

    fun deleteCategory(categoryName: String) {
        categories.remove(categoryName)
        val b = budgets.find { it.title.equals(categoryName, ignoreCase = true) }
        if (b != null) {
            budgets.remove(b)
        }
    }

    fun setBudgetLimit(categoryName: String, limitStr: String): Boolean {
        if (limitStr.isBlank()) {
            validationError = "Limit is required"
            return false
        }
        val limitVal = limitStr.toDoubleOrNull()
        if (limitVal == null) {
            validationError = "Enter a valid limit"
            return false
        }
        if (limitVal <= 0.0) {
            validationError = "Enter a limit greater than 0"
            return false
        }

        validationError = null
        val existing = budgets.find { it.title.equals(categoryName, ignoreCase = true) }
        if (existing != null) {
            existing.rawLimit = limitVal
            existing.limit = "MK %,d".format(limitVal.toInt())
            val rem = existing.rawLimit - existing.rawSpent
            existing.remaining = if (rem >= 0) "MK %,d left".format(rem.toInt()) else "MK %,d over".format((-rem).toInt())
            existing.progress = ((existing.rawSpent / existing.rawLimit).toFloat()).coerceIn(0f, 1f)
        } else {
            budgets.add(
                BudgetData(
                    title = categoryName,
                    spent = "MK 0",
                    rawSpent = 0.0,
                    limit = "MK %,d".format(limitVal.toInt()),
                    rawLimit = limitVal,
                    remaining = "MK %,d left".format(limitVal.toInt()),
                    progress = 0f,
                    targetPerDay = "MK %,d/day".format((limitVal / 30).toInt()),
                    icon = Icons.Default.Restaurant
                )
            )
        }
        return true
    }

    val filteredTransactions: List<TransactionData>
        get() {
            return when (analysisFilter) {
                "Day" -> transactions.take(1)
                "Week" -> transactions.take(3)
                "Month" -> transactions.filter { it.monthTag.equals(selectedReportMonth, ignoreCase = true) }
                "Semester" -> {
                    val start = if (activeSemesterNumber == 1) sem1StartMillis else sem2StartMillis
                    val end = if (activeSemesterNumber == 1) sem1EndMillis else sem2EndMillis
                    if (start == 0L || end == 0L) transactions else transactions.filter { it.timestamp in start..end }
                }
                else -> transactions
            }
        }

    val filteredExpenses: List<TransactionData>
        get() = filteredTransactions.filter { !it.isIncome }

    val filteredInflows: List<TransactionData>
        get() = filteredTransactions.filter { it.isIncome }

    val filteredSpentRaw: Double
        get() = filteredExpenses.sumOf { it.rawAmount }

    val filteredInflowRaw: Double
        get() = filteredInflows.sumOf { it.rawAmount }

    fun getSpendingByCategory(): List<ReportItemData> {
        val expenses = filteredExpenses
        val total = expenses.sumOf { it.rawAmount }.coerceAtLeast(1.0)
        val grouped = expenses.groupBy { it.category }
        if (grouped.isEmpty()) return listOf(ReportItemData("No Expenses Recorded", "MK 0", "0%"))
        return grouped.map { (cat, list) ->
            val sum = list.sumOf { it.rawAmount }
            val pct = ((sum / total) * 100).toInt()
            ReportItemData(cat, "MK %,d".format(sum.toInt()), "$pct%")
        }
    }

    fun getIncomeBySource(): List<ReportItemData> {
        val incomes = filteredInflows
        val total = incomes.sumOf { it.rawAmount }.coerceAtLeast(1.0)
        val grouped = incomes.groupBy { it.title }
        if (grouped.isEmpty()) return listOf(ReportItemData("No Income Recorded", "MK 0", "0%"))
        return grouped.map { (source, list) ->
            val sum = list.sumOf { it.rawAmount }
            val pct = ((sum / total) * 100).toInt()
            ReportItemData(source, "+MK %,d".format(sum.toInt()), "$pct%")
        }
    }
}
