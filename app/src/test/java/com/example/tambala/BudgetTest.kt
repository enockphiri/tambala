package com.example.tambala

import com.example.tambala.ui.viewmodel.FinanceViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BudgetTest {
    private lateinit var vm: FinanceViewModel

    @Before
    fun setUp() {
        vm = FinanceViewModel()
        vm.budgets.clear()
        vm.transactions.clear()
    }

    @Test
    fun test_TAM_BUDGET_001_CreateBudgetForPredefinedCategory() {
        val success = vm.setBudgetLimit("Food & Dining", "20000")
        assertTrue(success)
        val b = vm.budgets.find { it.title == "Food & Dining" }
        assertNotNull(b)
        assertEquals("MK 20,000", b?.limit)
        assertEquals("MK 0", b?.spent)
    }

    @Test
    fun test_TAM_BUDGET_002_CreateBudgetWithoutLimit() {
        val success = vm.setBudgetLimit("Food & Dining", "")
        assertFalse(success)
        assertEquals("Limit is required", vm.validationError)
    }

    @Test
    fun test_TAM_BUDGET_003_CreateBudgetWithZeroLimit() {
        val success = vm.setBudgetLimit("Food & Dining", "0")
        assertFalse(success)
        assertEquals("Enter a limit greater than 0", vm.validationError)
    }

    @Test
    fun test_TAM_BUDGET_004_BudgetSpentAndRemainingUpdateAfterExpense() {
        vm.setBudgetLimit("Food & Dining", "20000")
        vm.addTransaction("Lunch", "3000", false, "Food & Dining", System.currentTimeMillis(), "Today")
        val b = vm.budgets.find { it.title == "Food & Dining" }
        assertEquals("MK 3,000", b?.spent)
        assertEquals("MK 17,000 left", b?.remaining)
    }

    @Test
    fun test_TAM_BUDGET_005_SpendingEqualToBudgetLimit() {
        vm.setBudgetLimit("Food & Dining", "20000")
        vm.addTransaction("Groceries", "20000", false, "Food & Dining", System.currentTimeMillis(), "Today")
        val b = vm.budgets.find { it.title == "Food & Dining" }
        assertEquals("MK 20,000", b?.spent)
        assertEquals("MK 0 left", b?.remaining)
    }

    @Test
    fun test_TAM_BUDGET_006_EditBudgetLimit() {
        vm.setBudgetLimit("Food & Dining", "20000")
        val success = vm.setBudgetLimit("Food & Dining", "25000")
        assertTrue(success)
        val b = vm.budgets.find { it.title == "Food & Dining" }
        assertEquals("MK 25,000", b?.limit)
    }
}
