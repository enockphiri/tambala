package com.example.tambala

import com.example.tambala.ui.viewmodel.FinanceViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CategoryTest {
    private lateinit var vm: FinanceViewModel

    @Before
    fun setUp() {
        vm = FinanceViewModel()
        vm.categories.clear()
        vm.budgets.clear()
    }

    @Test
    fun test_TAM_CATEGORY_001_AddValidCategory() {
        val success = vm.addCategory("Books & Stationery")
        assertTrue(success)
        assertTrue(vm.categories.contains("Books & Stationery"))
        assertNull(vm.validationError)
    }

    @Test
    fun test_TAM_CATEGORY_002_AddEmptyCategoryName() {
        val success = vm.addCategory("   ")
        assertFalse(success)
        assertEquals("Category name cannot be empty", vm.validationError)
        assertEquals(0, vm.categories.size)
    }

    @Test
    fun test_TAM_CATEGORY_003_AddDuplicateCategory() {
        vm.addCategory("Transport")
        val success = vm.addCategory("Transport")
        assertFalse(success)
        assertEquals("Category already exists", vm.validationError)
        assertEquals(1, vm.categories.size)
    }

    @Test
    fun test_TAM_CATEGORY_004_EditCategoryNameUpdatesBudget() {
        vm.addCategory("Transport")
        vm.setBudgetLimit("Transport", "15000")

        val editSuccess = vm.editCategory("Transport", "Commute & Transit")
        assertTrue(editSuccess)
        assertTrue(vm.categories.contains("Commute & Transit"))
        assertFalse(vm.categories.contains("Transport"))

        val updatedBudget = vm.budgets.find { it.title == "Commute & Transit" }
        assertNotNull(updatedBudget)
    }

    @Test
    fun test_TAM_CATEGORY_005_EditCategoryWithEmptyName() {
        vm.addCategory("Transport")
        val editSuccess = vm.editCategory("Transport", "")
        assertFalse(editSuccess)
        assertEquals("Category name cannot be empty", vm.validationError)
        assertTrue(vm.categories.contains("Transport"))
    }

    @Test
    fun test_TAM_CATEGORY_006_DeleteCategoryRemovesCategoryAndBudget() {
        vm.addCategory("Transport")
        vm.setBudgetLimit("Transport", "10000")

        vm.deleteCategory("Transport")
        assertFalse(vm.categories.contains("Transport"))
        assertNull(vm.budgets.find { it.title == "Transport" })
    }
}
