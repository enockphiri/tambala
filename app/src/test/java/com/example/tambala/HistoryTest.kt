package com.example.tambala

import com.example.tambala.ui.viewmodel.FinanceViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class HistoryTest {
    private lateinit var vm: FinanceViewModel

    @Before
    fun setUp() {
        vm = FinanceViewModel()
        vm.transactions.clear()
        vm.addTransaction("Lunch", "2500", false, "Food & Dining", System.currentTimeMillis(), "Today")
        vm.addTransaction("Bus Fare", "500", false, "Transport", System.currentTimeMillis(), "Today")
    }

    @Test
    fun test_TAM_HISTORY_001_SearchTransactionByKeyword() {
        val query = "Lunch"
        val results = vm.transactions.filter { it.title.contains(query, ignoreCase = true) }
        assertEquals(1, results.size)
        assertEquals("Lunch", results.first().title)
    }

    @Test
    fun test_TAM_HISTORY_002_SearchWithNoMatchingResults() {
        val query = "zzzzzz"
        val results = vm.transactions.filter { it.title.contains(query, ignoreCase = true) }
        assertEquals(0, results.size)
    }
}
