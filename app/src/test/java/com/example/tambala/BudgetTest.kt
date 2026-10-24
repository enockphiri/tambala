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
    }

    @Test
    fun test_TAM_BUDGET_001_CreateBudgetForCategory() {
        val success = vm.setBudgetLimit("Food & Dining", "20000")
        assertTrue(success)
        val budget = vm.budgets.find { it.title == "Food & Dining" }
        assertNotNull(budget)
        assertEquals("MK 20,000", budget?.limit)
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
}
