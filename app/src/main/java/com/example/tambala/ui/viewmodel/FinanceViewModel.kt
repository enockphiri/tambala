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
    val title: String,
    val subtitle: String,
    val amount: String,
    val rawAmount: Double,
    val isIncome: Boolean,
    val category: String,
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

data class BreakdownItemData(
    val title: String,
    val percentage: String,
    val amount: String
)

class FinanceViewModel : ViewModel() {
    var balance by mutableStateOf("MK 354,200")
    var totalIncome by mutableStateOf("+MK 450,000")
    var totalExpenses by mutableStateOf("-MK 95,800")

    var rawBalance = 354200.0
    var rawIncome = 450000.0
    var rawExpenses = 95800.0

    // Settings state & Calendar calculated dates
    var semesterName by mutableStateOf("Semester 1 (2024/2025)")
    var startDateMillis by mutableStateOf(System.currentTimeMillis())
    var endDateMillis by mutableStateOf(System.currentTimeMillis() + 73L * 24 * 60 * 60 * 1000)

    var startDateStr by mutableStateOf(formatDate(System.currentTimeMillis()))
    var endDateStr by mutableStateOf(formatDate(System.currentTimeMillis() + 73L * 24 * 60 * 60 * 1000))
    var daysLeft by mutableIntStateOf(73)

    var monthlyLimit by mutableStateOf(120000.0)
    var abruptNotificationEnabled by mutableStateOf(true)
    var abruptThreshold by mutableStateOf(10000.0)

    // Analysis filter state ("Day", "Week", "Month", "Semester")
    var analysisFilter by mutableStateOf("Semester")

    val transactions = mutableStateListOf(
        TransactionData(1L, "Campus Cafeteria Lunch", "Oct 15 • Food", "-MK 1,800", 1800.0, false, "Food & Dining", Icons.Default.Restaurant),
        TransactionData(2L, "Photocopying Past Exams", "Oct 15 • Printing", "-MK 300", 300.0, false, "Academic Printing", Icons.Default.Print),
        TransactionData(3L, "2GB Campus Data Bundle", "Oct 14 • Airtime", "-MK 2,000", 2000.0, false, "Airtime & Data", Icons.Default.Wifi),
        TransactionData(4L, "HESLB Semester Allowance", "Oct 12 • Grant", "+MK 250,000", 250000.0, true, "Grant", Icons.Default.Restaurant),
        TransactionData(5L, "Sibling Upkeep Support", "Oct 10 • Allowance", "+MK 50,000", 50000.0, true, "Allowance", Icons.Default.Restaurant)
    )

    val budgets = mutableStateListOf(
        BudgetData("Food & Groceries", "MK 32,000", 32000.0, "MK 60,000", 60000.0, "MK 28,000 left", 0.53f, "MK 2,000/day", Icons.Default.Restaurant),
        BudgetData("Campus Transport", "MK 13,200", 13200.0, "MK 25,000", 25000.0, "MK 11,800 left", 0.72f, "MK 830/day", Icons.Default.DirectionsBus)
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

    private fun formatDate(millis: Long): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return formatter.format(millis)
    }

    fun updateStartDate(millis: Long) {
        startDateMillis = millis
        startDateStr = formatDate(millis)
        recalculateDaysLeft()
    }

    fun updateEndDate(millis: Long) {
        endDateMillis = millis
        endDateStr = formatDate(millis)
        recalculateDaysLeft()
    }

    private fun recalculateDaysLeft() {
        val diff = endDateMillis - System.currentTimeMillis()
        val calculated = (diff / (1000 * 60 * 60 * 24)).toInt()
        daysLeft = calculated.coerceAtLeast(1)
    }

    fun addTransaction(title: String, amountVal: Double, isIncome: Boolean, category: String) {
        val formattedAmount = if (isIncome) "+MK %,d".format(amountVal.toInt()) else "-MK %,d".format(amountVal.toInt())
        val icon = when (category) {
            "Food & Dining" -> Icons.Default.Restaurant
            "Academic Printing" -> Icons.Default.Print
            "Transport" -> Icons.Default.DirectionsBus
            "Airtime & Data" -> Icons.Default.Wifi
            else -> Icons.Default.Restaurant
        }

        transactions.add(0, TransactionData(System.currentTimeMillis(), title, "Today • $category", formattedAmount, amountVal, isIncome, category, icon))

        if (isIncome) {
            rawIncome += amountVal
            rawBalance += amountVal
        } else {
            rawExpenses += amountVal
            rawBalance -= amountVal

            if (abruptNotificationEnabled && amountVal >= abruptThreshold) {
                abruptAlertMessage = "⚠️ Abrupt Spending Alert: '$title' cost MK %,d (exceeds alert threshold of MK %,d)!".format(amountVal.toInt(), abruptThreshold.toInt())
            }

            val budget = budgets.find { it.title.equals(category, ignoreCase = true) }
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

    fun setBudgetLimit(categoryName: String, limitVal: Double) {
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
    }

    fun getBreakdownForFilter(filter: String): List<BreakdownItemData> {
        return when (filter) {
            "Day" -> listOf(
                BreakdownItemData("Food & Dining", "85%", "MK 1,800"),
                BreakdownItemData("Academic Printing", "15%", "MK 300")
            )
            "Week" -> listOf(
                BreakdownItemData("Food & Dining", "55%", "MK 8,000"),
                BreakdownItemData("Transport", "28%", "MK 4,000"),
                BreakdownItemData("Academic Printing", "17%", "MK 2,500")
            )
            "Month" -> listOf(
                BreakdownItemData("Food & Dining", "48%", "MK 22,000"),
                BreakdownItemData("Transport", "26%", "MK 12,000"),
                BreakdownItemData("Rent & Hostel", "26%", "MK 11,200")
            )
            else -> listOf(
                BreakdownItemData("Food & Dining", "36%", "MK 34,500"),
                BreakdownItemData("Rent & Hostel", "28%", "MK 26,800"),
                BreakdownItemData("Transport", "18%", "MK 17,200"),
                BreakdownItemData("Academic Printing", "11%", "MK 10,500"),
                BreakdownItemData("Airtime & Others", "7%", "MK 6,800")
            )
        }
    }
}
