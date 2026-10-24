package com.example.tambala

import com.example.tambala.ui.viewmodel.FinanceViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SemesterTest {
    private lateinit var vm: FinanceViewModel

    @Before
    fun setUp() {
        vm = FinanceViewModel()
    }

    @Test
    fun test_TAM_SEMESTER_001_SetSemesterEndDate() {
        val futureDate = System.currentTimeMillis() + 60L * 24 * 3600 * 1000
        vm.updateEndDate(futureDate)
        assertNull(vm.validationError)
    }

    @Test
    fun test_TAM_SEMESTER_004_SetSemesterEndDateInPast() {
        val pastDate = System.currentTimeMillis() - 86400000L * 2
        vm.updateEndDate(pastDate)
        assertEquals("End date cannot be in the past. Please choose a future date.", vm.validationError)
    }
}
