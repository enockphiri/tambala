package com.example.tambala.ui.viewmodel

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.example.tambala.data.FinanceStorage
import java.text.SimpleDateFormat
import java.util.Calendar
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
    var targetPerDay: String,
    val icon: ImageVector
)

data class ReportItemData(
    val title: String,
    val amount: String,
    val percentage: String
)

class FinanceViewModel(context: Context? = null) : ViewModel() {
    private val storage = FinanceStorage(context)

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

    var sem1StartMillis by mutableStateOf(System.currentTimeMillis() - 60L * 24 * 3600 * 1000)
    var sem1EndMillis by mutableStateOf(System.currentTimeMillis() + 60L * 24 * 3600 * 1000)

    var sem2StartMillis by mutableStateOf(System.currentTimeMillis() + 61L * 24 * 3600 * 1000)
    var sem2EndMillis by mutableStateOf(System.currentTimeMillis() + 180L * 24 * 3600 * 1000)

    val daysLeft: Int
        get() {
            val end = if (activeSemesterNumber == 1) sem1EndMillis else sem2EndMillis
            val diff = end - System.currentTimeMillis()
            val calculated = (diff / (1000 * 60 * 60 * 24)).toInt()
            return calculated.coerceAtLeast(1)
        }

    var monthlyLimit by mutableStateOf(120000.0)
    var abruptNotificationEnabled by mutableStateOf(true)
    var abruptThreshold by mutableStateOf(10000.0)

    // Analysis filter state ("Day", "Week", "Month", "Semester")
    var analysisFilter by mutableStateOf("Semester")

    // Jan-Dec available months for past & present reporting
    val availableMonths = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    var selectedReportMonth by mutableStateOf(SimpleDateFormat("MMMM", Locale.getDefault()).format(System.currentTimeMillis()))

    // Error state for validation matching Test Cases
    var validationError by mutableStateOf<String?>(null)

    // Notifications history list for Abrupt Spending and System alerts
    val notificationsList = mutableStateListOf<String>()

    val transactions = mutableStateListOf<TransactionData>()

    val budgets = mutableStateListOf<BudgetData>()

    val categories = mutableStateListOf<String>()

    var abruptAlertMessage by mutableStateOf<String?>(null)

    init {
        if (!storage.isInitialized()) {
            seedDefaultData()
            storage.setInitialized(true)
            saveData()
        } else {
            loadFromStorage()
        }
        recalculateTotals()
    }

    fun getCategoryIcon(categoryName: String): ImageVector {
        return when (categoryName.trim()) {
            "Food & Dining", "Food & Groceries", "Food", "Groceries", "Lunch", "Dinner" -> Icons.Default.Restaurant
            "Academic Printing", "Books", "Stationery", "School" -> Icons.Default.Print
            "Transport", "Bus", "Taxi", "Commute" -> Icons.Default.DirectionsBus
            "Airtime & Data", "WiFi", "Internet", "Data" -> Icons.Default.Wifi
            "Allowance", "Income Source", "Grant", "Salary", "Side Job", "Income", "Bank Transfer" -> Icons.Default.AccountBalanceWallet
            "Rent & Hostel", "Hostel", "Rent", "Housing" -> Icons.Default.Home
            "Project Costs", "Project", "Laptops", "Tech" -> Icons.Default.Build
            "Emergencies", "Health", "Medical" -> Icons.Default.MedicalServices
            "Shopping" -> Icons.Default.ShoppingCart
            else -> Icons.Default.Receipt
        }
    }

    fun saveData() {
        storage.saveSettings(
            activeSemesterNumber = activeSemesterNumber,
            sem1StartMillis = sem1StartMillis,
            sem1EndMillis = sem1EndMillis,
            sem2StartMillis = sem2StartMillis,
            sem2EndMillis = sem2EndMillis,
            monthlyLimit = monthlyLimit,
            abruptNotificationEnabled = abruptNotificationEnabled,
            abruptThreshold = abruptThreshold,
            selectedReportMonth = selectedReportMonth,
            analysisFilter = analysisFilter
        )
        storage.saveCategories(categories)
        storage.saveNotifications(notificationsList)
        storage.saveTransactions(transactions)
        storage.saveBudgets(budgets)
    }

    private fun seedDefaultData() {
        activeSemesterNumber = 1
        sem1StartMillis = System.currentTimeMillis() - 60L * 24 * 3600 * 1000
        sem1EndMillis = System.currentTimeMillis() + 60L * 24 * 3600 * 1000
        sem2StartMillis = System.currentTimeMillis() + 61L * 24 * 3600 * 1000
        sem2EndMillis = System.currentTimeMillis() + 180L * 24 * 3600 * 1000

        notificationsList.clear()
        notificationsList.addAll(
            listOf(
                "🔔 Welcome to Tambala! 100% offline student upkeep tracker is active.",
                "💡 Tip: Set your budget limits in the Budgets tab to monitor abrupt spending."
            )
        )

        categories.clear()
        categories.addAll(
            listOf(
                "Food & Dining",
                "Rent & Hostel",
                "Academic Printing",
                "Transport",
                "Airtime & Data",
                "Project Costs",
                "Emergencies"
            )
        )

        transactions.clear()
        val now = System.currentTimeMillis()
        val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(now)
        transactions.addAll(
            listOf(
                TransactionData(1L, now, "Lunch", "Oct 15 • Food & Dining", "-MK 2,500", 2500.0, false, "Food & Dining", currentMonth, getCategoryIcon("Food & Dining")),
                TransactionData(2L, now, "Photocopying Past Exams", "Oct 15 • Academic Printing", "-MK 300", 300.0, false, "Academic Printing", currentMonth, getCategoryIcon("Academic Printing")),
                TransactionData(3L, now, "2GB Campus Data Bundle", "Oct 14 • Airtime & Data", "-MK 2,000", 2000.0, false, "Airtime & Data", currentMonth, getCategoryIcon("Airtime & Data")),
                TransactionData(4L, now, "Allowance", "Oct 12 • Allowance", "+MK 50,000", 50000.0, true, "Allowance", currentMonth, getCategoryIcon("Allowance")),
                TransactionData(5L, now, "Side Job", "Oct 10 • Grant", "+MK 10,000", 10000.0, true, "Grant", currentMonth, getCategoryIcon("Grant"))
            )
        )

        budgets.clear()
        budgets.addAll(
            listOf(
                BudgetData("Food & Groceries", "MK 2,500", 2500.0, "MK 20,000", 20000.0, "MK 17,500 left", 0.125f, "MK 333/day", getCategoryIcon("Food & Groceries")),
                BudgetData("Transport", "MK 0", 0.0, "MK 10,000", 10000.0, "MK 10,000 left", 0f, "MK 166/day", getCategoryIcon("Transport"))
            )
        )
    }

    private fun loadFromStorage() {
        activeSemesterNumber = storage.getInt("activeSemesterNumber", 1)
        sem1StartMillis = storage.getLong("sem1StartMillis", System.currentTimeMillis() - 60L * 24 * 3600 * 1000)
        sem1EndMillis = storage.getLong("sem1EndMillis", System.currentTimeMillis() + 60L * 24 * 3600 * 1000)
        sem2StartMillis = storage.getLong("sem2StartMillis", System.currentTimeMillis() + 61L * 24 * 3600 * 1000)
        sem2EndMillis = storage.getLong("sem2EndMillis", System.currentTimeMillis() + 180L * 24 * 3600 * 1000)

        monthlyLimit = storage.getDouble("monthlyLimit", 120000.0)
        abruptNotificationEnabled = storage.getBoolean("abruptNotificationEnabled", true)
        abruptThreshold = storage.getDouble("abruptThreshold", 10000.0)
        selectedReportMonth = storage.getString("selectedReportMonth", SimpleDateFormat("MMMM", Locale.getDefault()).format(System.currentTimeMillis()))
        analysisFilter = storage.getString("analysisFilter", "Semester")

        val loadedCategories = storage.loadCategories()
        categories.clear()
        categories.addAll(loadedCategories)

        val loadedNotifications = storage.loadNotifications()
        notificationsList.clear()
        notificationsList.addAll(loadedNotifications)

        val loadedTxObjects = storage.loadTransactions()
        transactions.clear()
        for (obj in loadedTxObjects) {
            val cat = obj.optString("category", "Other")
            val isInc = obj.optBoolean("isIncome", false)
            val tx = TransactionData(
                id = obj.optLong("id", System.currentTimeMillis()),
                timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                title = obj.optString("title", ""),
                subtitle = obj.optString("subtitle", ""),
                amount = obj.optString("amount", "MK 0"),
                rawAmount = obj.optDouble("rawAmount", 0.0),
                isIncome = isInc,
                category = cat,
                monthTag = obj.optString("monthTag", selectedReportMonth),
                icon = getCategoryIcon(cat)
            )
            transactions.add(tx)
        }

        val loadedBudgetObjects = storage.loadBudgets()
        budgets.clear()
        for (obj in loadedBudgetObjects) {
            val title = obj.optString("title", "")
            val limitVal = obj.optDouble("rawLimit", 0.0)
            budgets.add(
                BudgetData(
                    title = title,
                    spent = "MK 0",
                    rawSpent = 0.0,
                    limit = "MK %,d".format(limitVal.toInt()),
                    rawLimit = limitVal,
                    remaining = "MK %,d left".format(limitVal.toInt()),
                    progress = 0f,
                    targetPerDay = "MK 0/day",
                    icon = getCategoryIcon(title)
                )
            )
        }
    }

    fun recalculateTotals() {
        rawIncome = transactions.filter { it.isIncome }.sumOf { it.rawAmount }
        rawExpenses = transactions.filter { !it.isIncome }.sumOf { it.rawAmount }
        rawBalance = rawIncome - rawExpenses

        totalIncome = "+MK %,d".format(rawIncome.toInt())
        totalExpenses = "-MK %,d".format(rawExpenses.toInt())
        balance = "MK %,d".format(rawBalance.toInt())

        recalculateBudgets()
    }

    fun recalculateBudgets() {
        val remainingDays = daysLeft.coerceAtLeast(1)
        for (budget in budgets) {
            val totalSpentForCategory = transactions
                .filter { !it.isIncome && it.category.equals(budget.title, ignoreCase = true) }
                .sumOf { it.rawAmount }

            budget.rawSpent = totalSpentForCategory
            budget.spent = "MK %,d".format(budget.rawSpent.toInt())
            budget.limit = "MK %,d".format(budget.rawLimit.toInt())

            val rem = budget.rawLimit - budget.rawSpent
            budget.remaining = if (rem >= 0) "MK %,d left".format(rem.toInt()) else "MK %,d over".format((-rem).toInt())
            budget.progress = if (budget.rawLimit > 0) ((budget.rawSpent / budget.rawLimit).toFloat()).coerceIn(0f, 1f) else 0f

            val dailyTarget = (budget.rawLimit / remainingDays).toInt()
            budget.targetPerDay = "MK %,d/day".format(dailyTarget)
        }
    }

    fun addNotification(msg: String) {
        notificationsList.add(0, msg)
        saveData()
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
        recalculateBudgets()
        saveData()
    }

    fun updateEndDate(millis: Long) {
        if (millis < System.currentTimeMillis()) {
            validationError = "End date cannot be in the past. Please choose a future date."
            return
        }
        validationError = null
        if (activeSemesterNumber == 1) {
            sem1EndMillis = millis
        } else {
            sem2EndMillis = millis
        }
        recalculateBudgets()
        saveData()
    }

    fun updateActiveSemester(semNum: Int) {
        activeSemesterNumber = semNum
        recalculateBudgets()
        saveData()
    }

    fun updateAbruptAlerts(enabled: Boolean, threshold: Double? = null) {
        abruptNotificationEnabled = enabled
        if (threshold != null && threshold > 0) {
            abruptThreshold = threshold
        }
        saveData()
    }

    fun updateAnalysisFilter(filter: String) {
        analysisFilter = filter
        saveData()
    }

    fun updateSelectedReportMonth(month: String) {
        selectedReportMonth = month
        saveData()
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
        val finalCategory = if (isIncome) {
            if (category.isNotBlank() && category != "Food & Dining" && category != "General" && category != "Income") category else "Allowance"
        } else {
            if (category.isBlank()) "General" else category
        }
        val icon = getCategoryIcon(finalCategory)

        val subtitleStr = "$dateStr • $finalCategory"
        val txMonthTag = SimpleDateFormat("MMMM", Locale.getDefault()).format(dateMillis)
        transactions.add(0, TransactionData(System.currentTimeMillis(), dateMillis, title.ifBlank { finalCategory }, subtitleStr, formattedAmount, amountVal, isIncome, finalCategory, txMonthTag, icon))

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
        }

        totalIncome = "+MK %,d".format(rawIncome.toInt())
        totalExpenses = "-MK %,d".format(rawExpenses.toInt())
        balance = "MK %,d".format(rawBalance.toInt())

        recalculateBudgets()
        saveData()
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
            }

            totalIncome = "+MK %,d".format(rawIncome.toInt())
            totalExpenses = "-MK %,d".format(rawExpenses.toInt())
            balance = "MK %,d".format(rawBalance.toInt())

            recalculateBudgets()
            saveData()
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
            }
            totalIncome = "+MK %,d".format(rawIncome.toInt())
            totalExpenses = "-MK %,d".format(rawExpenses.toInt())
            balance = "MK %,d".format(rawBalance.toInt())

            transactions.remove(tx)
            recalculateBudgets()
            saveData()
        }
    }

    fun addCategory(categoryName: String): Boolean {
        val trimmed = categoryName.trim()
        if (trimmed.isBlank()) {
            validationError = "Category name cannot be empty"
            return false
        }
        if (categories.any { it.equals(trimmed, ignoreCase = true) }) {
            validationError = "Category already exists"
            return false
        }
        validationError = null
        categories.add(trimmed)
        saveData()
        return true
    }

    fun editCategory(oldName: String, newName: String): Boolean {
        val trimmed = newName.trim()
        if (trimmed.isBlank()) {
            validationError = "Category name cannot be empty"
            return false
        }
        val index = categories.indexOfFirst { it.equals(oldName, ignoreCase = true) }
        if (index != -1) {
            categories[index] = trimmed

            val budget = budgets.find { it.title.equals(oldName, ignoreCase = true) }
            if (budget != null) {
                val idx = budgets.indexOf(budget)
                budgets[idx] = budget.copy(title = trimmed)
            }

            recalculateBudgets()
            saveData()
            return true
        }
        return false
    }

    fun deleteCategory(categoryName: String) {
        categories.removeAll { it.equals(categoryName, ignoreCase = true) }
        val budget = budgets.find { it.title.equals(categoryName, ignoreCase = true) }
        if (budget != null) {
            budgets.remove(budget)
        }
        recalculateBudgets()
        saveData()
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
                    targetPerDay = "MK 0/day",
                    icon = getCategoryIcon(categoryName)
                )
            )
        }

        recalculateBudgets()
        saveData()
        return true
    }

    val totalCategoryBudgetsRaw: Double
        get() = budgets.sumOf { it.rawLimit }

    fun updateMonthlyLimit(limitStr: String): Boolean {
        if (limitStr.isBlank()) {
            validationError = "Monthly allocation is required"
            return false
        }
        val amountVal = limitStr.toDoubleOrNull()
        if (amountVal == null) {
            validationError = "Enter a valid monthly allocation"
            return false
        }
        if (amountVal <= 0.0) {
            validationError = "Enter an allocation greater than 0"
            return false
        }

        validationError = null
        monthlyLimit = amountVal
        saveData()
        return true
    }

    fun deleteBudget(categoryName: String) {
        val budget = budgets.find { it.title.equals(categoryName, ignoreCase = true) }
        if (budget != null) {
            budgets.remove(budget)
            recalculateBudgets()
            saveData()
        }
    }

    // Dynamic filtering for Day, Week, Month (Jan-Dec), Semester 1 & Semester 2 based on start/end date range
    val filteredTransactions: List<TransactionData>
        get() {
            val now = System.currentTimeMillis()
            return when (analysisFilter) {
                "Day" -> {
                    val calNow = Calendar.getInstance().apply { timeInMillis = now }
                    transactions.filter { tx ->
                        val calTx = Calendar.getInstance().apply { timeInMillis = tx.timestamp }
                        calNow.get(Calendar.YEAR) == calTx.get(Calendar.YEAR) &&
                        calNow.get(Calendar.DAY_OF_YEAR) == calTx.get(Calendar.DAY_OF_YEAR)
                    }
                }
                "Week" -> {
                    val sevenDaysAgo = now - 7L * 24 * 3600 * 1000
                    transactions.filter { tx -> tx.timestamp in sevenDaysAgo..now }
                }
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
