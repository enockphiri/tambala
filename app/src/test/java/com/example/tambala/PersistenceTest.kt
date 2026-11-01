package com.example.tambala

import com.example.tambala.ui.viewmodel.FinanceViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PersistenceTest {
    private lateinit var vm: FinanceViewModel

    @Before
    fun setUp() {
        vm = FinanceViewModel()
        vm.transactions.clear()
        vm.budgets.clear()
        vm.categories.clear()
        vm.recalculateTotals()
    }

    @Test
    fun test_TAM_PERSISTENCE_001_DynamicDaysLeftCalculation() {
        // Set sem1EndMillis to 10 days in the future
        val tenDaysInFuture = System.currentTimeMillis() + 10L * 24 * 3600 * 1000
        vm.updateEndDate(tenDaysInFuture)

        // daysLeft should evaluate to 10
        assertEquals(10, vm.daysLeft)
    }

    @Test
    fun test_TAM_PERSISTENCE_002_BudgetSpentRecalculatedFromTransactions() {
        vm.setBudgetLimit("Food & Dining", "20000")
        vm.addTransaction("Lunch", "5000", false, "Food & Dining", System.currentTimeMillis(), "Today")

        val b = vm.budgets.find { it.title == "Food & Dining" }
        assertNotNull(b)
        assertEquals("MK 5,000", b?.spent)
        assertEquals("MK 15,000 left", b?.remaining)

        // Deleting the transaction should update the budget back to 0
        val txId = vm.transactions.first().id
        vm.deleteTransaction(txId)

        assertEquals("MK 0", b?.spent)
        assertEquals("MK 20,000 left", b?.remaining)
    }

    @Test
    fun test_TAM_PERSISTENCE_003_DeleteTransactionRemovesIt() {
        vm.addTransaction("Income 1", "100000", true, "Allowance", System.currentTimeMillis(), "Today")
        vm.addTransaction("Book", "15000", false, "Academic Printing", System.currentTimeMillis(), "Today")

        assertEquals(2, vm.transactions.size)
        assertEquals("MK 85,000", vm.balance)

        val expenseTx = vm.transactions.first { !it.isIncome }
        vm.deleteTransaction(expenseTx.id)

        assertEquals(1, vm.transactions.size)
        assertEquals("MK 100,000", vm.balance)
    }
}
