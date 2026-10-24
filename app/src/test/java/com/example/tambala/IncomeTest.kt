package com.example.tambala

import com.example.tambala.ui.viewmodel.FinanceViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class IncomeTest {
    private lateinit var vm: FinanceViewModel

    @Before
    fun setUp() {
        vm = FinanceViewModel()
        vm.transactions.clear()
        vm.rawIncome = 0.0
        vm.rawExpenses = 0.0
        vm.rawBalance = 0.0
        vm.totalIncome = "MK 0"
        vm.totalExpenses = "-MK 0"
        vm.balance = "MK 0"
    }

    @Test
    fun test_TAM_INCOME_001_RecordIncomeWithValidData() {
        val success = vm.addTransaction("Start of semester", "50000", true, "Allowance", System.currentTimeMillis(), "Today")
        assertTrue(success)
        assertEquals(1, vm.transactions.size)
        assertEquals("+MK 50,000", vm.totalIncome)
        assertEquals("MK 50,000", vm.balance)
        assertEquals("Start of semester", vm.transactions.first().title)
    }

    @Test
    fun test_TAM_INCOME_002_RecordIncomeWithoutAmount() {
        val success = vm.addTransaction("Allowance", "", true, "Allowance", System.currentTimeMillis(), "Today")
        assertFalse(success)
        assertEquals("Amount is required", vm.validationError)
        assertEquals(0, vm.transactions.size)
    }
}
