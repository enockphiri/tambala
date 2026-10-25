package com.example.tambala

import com.example.tambala.ui.viewmodel.FinanceViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TransactionTest {
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
        vm.addTransaction("Lunch", "2500", false, "Food & Dining", System.currentTimeMillis(), "Today")
    }

    @Test
    fun test_TAM_TRANSACTION_001_EditTransaction() {
        val txId = vm.transactions.first().id
        val success = vm.editTransaction(txId, "Lunch", "3000")
        assertTrue(success)
        assertEquals("-MK 3,000", vm.totalExpenses)
        assertEquals("MK 47,000", vm.balance)
    }

    @Test
    fun test_TAM_TRANSACTION_003_DeleteTransaction() {
        val txId = vm.transactions.first().id
        vm.deleteTransaction(txId)
        assertEquals(0, vm.transactions.size)
        assertEquals("-MK 0", vm.totalExpenses)
        assertEquals("MK 50,000", vm.balance)
    }
}
