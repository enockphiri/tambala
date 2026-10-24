package com.example.tambala

import com.example.tambala.ui.viewmodel.FinanceViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BalanceTest {
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
    fun test_TAM_BALANCE_001_BalanceUpdatesAfterAddingIncome() {
        vm.addTransaction("Allowance", "50000", true, "Allowance", System.currentTimeMillis(), "Today")
        assertEquals("+MK 50,000", vm.totalIncome)
        assertEquals("-MK 0", vm.totalExpenses)
        assertEquals("MK 50,000", vm.balance)
    }

    @Test
    fun test_TAM_BALANCE_002_BalanceUpdatesAfterAddingExpense() {
        vm.addTransaction("Allowance", "50000", true, "Allowance", System.currentTimeMillis(), "Today")
        vm.addTransaction("Lunch", "2500", false, "Food & Dining", System.currentTimeMillis(), "Today")
        assertEquals("+MK 50,000", vm.totalIncome)
        assertEquals("-MK 2,500", vm.totalExpenses)
        assertEquals("MK 47,500", vm.balance)
    }
}
