package com.example.tambala.ui.pages

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tambala.ui.components.BottomNavBar
import com.example.tambala.ui.components.BudgetProgressItem
import com.example.tambala.ui.components.TopNavBar
import com.example.tambala.ui.viewmodel.BudgetData
import com.example.tambala.ui.viewmodel.FinanceViewModel

@Composable
fun BudgetsScreen(
    viewModel: FinanceViewModel = remember { FinanceViewModel() },
    onNavigateToTab: (Int) -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    var showLimitDialog by remember { mutableStateOf(false) }
    var showEditLimitDialog by remember { mutableStateOf(false) }
    var selectedCategoryForLimit by remember { mutableStateOf("") }
    var selectedBudgetToEdit by remember { mutableStateOf<BudgetData?>(null) }
    var limitInput by remember { mutableStateOf("") }

    if (showLimitDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.validationError = null
                showLimitDialog = false
            },
            title = { Text("Set Budget Limit for '$selectedCategoryForLimit'", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (viewModel.validationError != null) {
                        Text(
                            text = viewModel.validationError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    OutlinedTextField(
                        value = limitInput,
                        onValueChange = { limitInput = it },
                        label = { Text("Budget Limit (MK) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val success = viewModel.setBudgetLimit(selectedCategoryForLimit, limitInput)
                        if (success) {
                            limitInput = ""
                            selectedCategoryForLimit = ""
                            showLimitDialog = false
                        }
                    }
                ) {
                    Text("Save Limit", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.validationError = null
                        showLimitDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showEditLimitDialog && selectedBudgetToEdit != null) {
        AlertDialog(
            onDismissRequest = {
                viewModel.validationError = null
                showEditLimitDialog = false
            },
            title = { Text("Edit Budget Limit for '${selectedBudgetToEdit!!.title}'", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (viewModel.validationError != null) {
                        Text(
                            text = viewModel.validationError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    OutlinedTextField(
                        value = limitInput,
                        onValueChange = { limitInput = it },
                        label = { Text("New Budget Limit (MK) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val success = viewModel.setBudgetLimit(selectedBudgetToEdit!!.title, limitInput)
                        if (success) {
                            limitInput = ""
                            selectedBudgetToEdit = null
                            showEditLimitDialog = false
                        }
                    }
                ) {
                    Text("Update Limit", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextButton(
                        onClick = {
                            viewModel.deleteBudget(selectedBudgetToEdit!!.title)
                            limitInput = ""
                            selectedBudgetToEdit = null
                            showEditLimitDialog = false
                        }
                    ) {
                        Text("Delete Budget", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                    }
                    TextButton(
                        onClick = {
                            viewModel.validationError = null
                            selectedBudgetToEdit = null
                            showEditLimitDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopNavBar(
                title = "Budgets",
                subtitle = viewModel.semesterName,
                badgeText = "${viewModel.daysLeft} days left",
                onProfileClick = onSettingsClick
            )
        },
        bottomBar = {
            BottomNavBar(items = tambalaBottomNavItems, selectedIndex = 1, onItemSelected = onNavigateToTab)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Monthly Snapshot Card (Read-only summary card)
            item {
                val isOverBudget = viewModel.rawExpenses > viewModel.monthlyLimit
                val overAmount = viewModel.rawExpenses - viewModel.monthlyLimit
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(
                        1.dp,
                        if (isOverBudget) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "MONTHLY SNAPSHOT",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (isOverBudget) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "⚠️ Over Budget by MK %,d!".format(overAmount.toInt()),
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Total Upkeep", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("MK %,d".format(viewModel.monthlyLimit.toInt()), color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Category Budgets", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("MK %,d".format(viewModel.totalCategoryBudgetsRaw.toInt()), color = MaterialTheme.colorScheme.secondary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Spent", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    viewModel.totalExpenses.replace("-", ""),
                                    color = if (isOverBudget) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Active Budgets Header
            item {
                Text(
                    text = "Active Budgets",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Active Budget Items from ViewModel (Clickable to edit limit)
            items(viewModel.budgets) { b ->
                BudgetProgressItem(
                    title = b.title,
                    spent = b.spent,
                    limit = b.limit,
                    remaining = b.remaining,
                    progress = b.progress,
                    icon = b.icon,
                    targetPerDay = b.targetPerDay,
                    onClick = {
                        viewModel.validationError = null
                        selectedBudgetToEdit = b
                        limitInput = b.rawLimit.toInt().toString()
                        showEditLimitDialog = true
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Unbudgeted Categories Header
            item {
                Text(
                    text = "Categories & Unbudgeted",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Categories list where user can tap "+ Set Limit"
            items(viewModel.categories) { cat ->
                val hasBudget = viewModel.budgets.any { it.title.equals(cat, ignoreCase = true) }
                if (!hasBudget) {
                    UnbudgetedItem(
                        title = cat,
                        onSetLimitClick = {
                            viewModel.validationError = null
                            selectedCategoryForLimit = cat
                            limitInput = ""
                            showLimitDialog = true
                        }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun UnbudgetedItem(
    title: String,
    onSetLimitClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onSetLimitClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "+ Set Limit",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BudgetsScreenPreview() {
    BudgetsScreen()
}
