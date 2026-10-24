package com.example.tambala.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Wifi
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
    var balance by mutableStateOf("MK 354,200")
    var totalIncome by mutableStateOf("+MK 450,000")
    var totalExpenses by mutableStateOf("-MK 95,800")

    var rawBalance = 354200.0
    var rawIncome = 450000.0
    var rawExpenses = 95800.0

    // Dynamic Semester configuration (1 or 2, without hardcoded years)
    var activeSemesterNumber by mutableStateOf(1) // 1 or 2
    
    val semesterName: String
        get() = "Semester $activeSemesterNumber"

    var sem1StartMillis by mutableStateOf(System.currentTimeMillis() - 60L * 24 * 3600 * 1000)
    var sem1EndMillis by mutableStateOf(System.currentTimeMillis() + 60L * 24 * 3600 * 1000)

    var sem2StartMillis by mutableStateOf(System.currentTimeMillis() + 61L * 24 * 3600 * 1000)
    var sem2EndMillis by mutableStateOf(System.currentTimeMillis() + 180L * 24 * 3600 * 1000)

    var daysLeft by mutableIntStateOf(60)

    var monthlyLimit by mutableStateOf(120000.0)
    var abruptNotificationEnabled by mutableStateOf(true)
    var abruptThreshold by mutableStateOf(10000.0)

    // Analysis filter state ("Day", "Week", "Month", "Semester")
    var analysisFilter by mutableStateOf("Semester")

    // Jan-Dec available months for past & present reporting
    val availableMonths = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    var selectedReportMonth by mutableStateOf("November")

    // Error state for validation matching Test Cases
    var validationError by mutableStateOf<String?>(null)

    // Notifications history list for Abrupt Spending and System alerts
    val notificationsList = mutableStateListOf(
        "🔔 Welcome to Tambala! 100% offline student upkeep tracker is active.",
        "💡 Tip: Set your budget limits in the Budgets tab to monitor abrupt spending."
    )

    val transactions = mutableStateListOf(
        TransactionData(1L, System.currentTimeMillis(), "Lunch", "Oct 15 • Food & Dining", "-MK 2,500", 2500.0, false, "Food & Dining", "October", Icons.Default.Restaurant),
        TransactionData(2L, System.currentTimeMillis(), "Photocopying Past Exams", "Oct 15 • Academic Printing", "-MK 300", 300.0, false, "Academic Printing", "October", Icons.Default.Print),
        TransactionData(3L, System.currentTimeMillis(), "2GB Campus Data Bundle", "Oct 14 • Airtime & Data", "-MK 2,000", 2000.0, false, "Airtime & Data", "October", Icons.Default.Wifi),
        TransactionData(4L, System.currentTimeMillis(), "Allowance", "Oct 12 • Allowance", "+MK 50,000", 50000.0, true, "Allowance", "October", Icons.Default.Restaurant),
        TransactionData(5L, System.currentTimeMillis(), "Side Job", "Oct 10 • Grant", "+MK 10,000", 10000.0, true, "Grant", "October", Icons.Default.Restaurant)
    )

    val budgets = mutableStateListOf(
        BudgetData("Food & Groceries", "MK 2,500", 2500.0, "MK 20,000", 20000.0, "MK 17,500 left", 0.125f, "MK 666/day", Icons.Default.Restaurant),
        BudgetData("Transport", "MK 0", 0.0, "MK 10,000", 10000.0, "MK 10,000 left", 0f, "MK 333/day", Icons.Default.DirectionsBus)
    )

    val categories = mutableStateListOf(
        "Food & Dining",
        "Rent & Hostel",
        "Academic Printing",
        "Transport",
        "Airtime & Data",
        "Project Costs",
        "Emergencies"
    )

    var abruptAlertMessage by mutableStateOf<String?>(null)

    fun addNotification(msg: String) {
        notificationsList.add(0, msg)
    }

    private fun formatDate(millis: Long): String {
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
        val diff = end - System.currentTimeMillis()
        val calculated = (diff / (1000 * 60 * 60 * 24)).toInt()
        daysLeft = calculated.coerceAtLeast(1)
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
        val icon = when (finalCategory) {
            "Food & Dining" -> Icons.Default.Restaurant
            "Academic Printing" -> Icons.Default.Print
            "Transport" -> Icons.Default.DirectionsBus
            "Airtime & Data" -> Icons.Default.Wifi
            else -> Icons.Default.Restaurant
        }

        val subtitleStr = "$dateStr • $finalCategory"
        transactions.add(0, TransactionData(System.currentTimeMillis(), dateMillis, title.ifBlank { finalCategory }, subtitleStr, formattedAmount, amountVal, isIncome, finalCategory, selectedReportMonth, icon))

        if (isIncome) {
            rawIncome += amountVal
            rawBalance += amountVal
        } else {
            rawExpenses += amountVal
            rawBalance -= amountVal

            // Abrupt Spending Notification & Alert Trigger
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

                // Abrupt check on edit
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
            totalIncome = "+MK %,d".format(rawIncome.toInt())
            totalExpenses = "-MK %,d".format(rawExpenses.toInt())
            balance = "MK %,d".format(rawBalance.toInt())
            transactions.remove(tx)
        }
    }

    fun addCategory(categoryName: String) {
        if (categoryName.isNotBlank() && !categories.contains(categoryName)) {
            categories.add(categoryName)
        }
    }

    fun editCategory(oldName: String, newName: String) {
        val index = categories.indexOf(oldName)
        if (index != -1 && newName.isNotBlank()) {
            categories[index] = newName
        }
    }

    fun deleteCategory(categoryName: String) {
        categories.remove(categoryName)
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

    // Dynamic filtering for Day, Week, Month (Jan-Dec), Semester 1 & Semester 2 based on start/end date range
    val filteredTransactions: List<TransactionData>
        get() {
            return when (analysisFilter) {
                "Day" -> transactions.take(1)
                "Week" -> transactions.take(3)
                "Month" -> transactions.filter { it.monthTag.equals(selectedReportMonth, ignoreCase = true) }
                "Semester" -> {
                    val start = if (activeSemesterNumber == 1) sem1StartMillis else sem2StartMillis
                    val end = if (activeSemesterNumber == 1) sem1EndMillis else sem2EndMillis
                    transactions.filter { it.timestamp in start..end }
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
