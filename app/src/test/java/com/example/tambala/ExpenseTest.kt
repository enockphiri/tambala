package com.example.tambala

import com.example.tambala.ui.viewmodel.FinanceViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExpenseTest {
    private lateinit var vm: FinanceViewModel

    @Before
    fun setUp() {
        vm = FinanceViewModel()
        vm.transactions.clear()
        vm.rawIncome = 50000.0
        vm.rawExpenses = 0.0
        vm.rawBalance = 50000.0
        vm.totalIncome = "+MK 50,000"
        vm.totalExpenses = "-MK 0"
        vm.balance = "MK 50,000"
    }

    @Test
    fun test_TAM_EXPENSE_001_RecordExpenseWithValidData() {
        val success = vm.addTransaction("Lunch", "2500", false, "Food & Dining", System.currentTimeMillis(), "Today")
        assertTrue(success)
        assertEquals("-MK 2,500", vm.totalExpenses)
        assertEquals("MK 47,500", vm.balance)
        assertEquals("Lunch", vm.transactions.first().title)
    }

    @Test
    fun test_TAM_EXPENSE_002_RecordExpenseWithoutAmount() {
        val success = vm.addTransaction("Lunch", "", false, "Food & Dining", System.currentTimeMillis(), "Today")
        assertFalse(success)
        assertEquals("Amount is required", vm.validationError)
    }

    @Test
    fun test_TAM_EXPENSE_003_RecordExpenseWithZeroAmount() {
        val success = vm.addTransaction("Lunch", "0", false, "Food & Dining", System.currentTimeMillis(), "Today")
        assertFalse(success)
        assertEquals("Enter an amount greater than 0", vm.validationError)
    }
}
