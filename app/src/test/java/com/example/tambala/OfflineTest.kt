package com.example.tambala

import com.example.tambala.ui.viewmodel.FinanceViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class OfflineTest {
    private lateinit var vm: FinanceViewModel

    @Before
    fun setUp() {
        vm = FinanceViewModel()
    }

    @Test
    fun test_TAM_OFFLINE_001_CoreFunctionsOffline() {
        val success = vm.addTransaction("Offline Expense", "2000", false, "Food & Dining", System.currentTimeMillis(), "Today")
        assertTrue(success)
        val budgetSuccess = vm.setBudgetLimit("Food & Dining", "15000")
        assertTrue(budgetSuccess)
        val reports = vm.getSpendingByCategory()
        assertNotNull(reports)
    }
}
