package com.example.tambala

import com.example.tambala.ui.viewmodel.FinanceViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ReportTest {
    private lateinit var vm: FinanceViewModel

    @Before
    fun setUp() {
        vm = FinanceViewModel()
        vm.transactions.clear()
        vm.addTransaction("Lunch", "3000", false, "Food & Dining", System.currentTimeMillis(), "Today")
        vm.addTransaction("Taxi", "1000", false, "Transport", System.currentTimeMillis(), "Today")
    }

    @Test
    fun test_TAM_REPORT_001_FinancialTrendAndSpendingReport() {
        vm.analysisFilter = "Month"
        val spending = vm.getSpendingByCategory()
        assertTrue(spending.isNotEmpty())
        assertEquals(2, spending.size)
    }
}
