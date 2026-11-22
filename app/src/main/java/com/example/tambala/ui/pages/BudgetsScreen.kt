package com.example.tambala.ui.pages

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tambala.ui.components.BottomNavBar
import com.example.tambala.ui.components.BudgetProgressItem
import com.example.tambala.ui.components.TopNavBar
import com.example.tambala.ui.theme.Background
import com.example.tambala.ui.theme.CardBorder
import com.example.tambala.ui.theme.ExpenseCoral
import com.example.tambala.ui.theme.IndigoPrimary
import com.example.tambala.ui.theme.TextPrimary
import com.example.tambala.ui.theme.TextSecondary
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
    var showMonthlyLimitDialog by remember { mutableStateOf(false) }
    var selectedCategoryForLimit by remember { mutableStateOf("") }
    var selectedBudgetToEdit by remember { mutableStateOf<BudgetData?>(null) }
    var limitInput by remember { mutableStateOf("") }
    var monthlyLimitInput by remember { mutableStateOf("") }

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
                            color = Color(0xFFE11D48),
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
                    Text("Save Limit", fontWeight = FontWeight.Bold, color = IndigoPrimary)
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

    if (showMonthlyLimitDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.validationError = null
                showMonthlyLimitDialog = false
            },
            title = { Text("Set Total Monthly Upkeep", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (viewModel.validationError != null) {
                        Text(
                            text = viewModel.validationError ?: "",
                            color = Color(0xFFE11D48),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    OutlinedTextField(
                        value = monthlyLimitInput,
                        onValueChange = { monthlyLimitInput = it },
                        label = { Text("Total Monthly Upkeep (MK) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val success = viewModel.updateMonthlyLimit(monthlyLimitInput)
                        if (success) {
                            monthlyLimitInput = ""
                            showMonthlyLimitDialog = false
                        }
                    }
                ) {
                    Text("Save Allocation", fontWeight = FontWeight.Bold, color = IndigoPrimary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.validationError = null
                        showMonthlyLimitDialog = false
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
                            color = Color(0xFFE11D48),
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
                    Text("Update Limit", fontWeight = FontWeight.Bold, color = IndigoPrimary)
                }
            },
            dismissButton = {
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
        )
    }

    Scaffold(
        topBar = {
            TopNavBar(
                title = "Budgets",
                subtitle = viewModel.semesterName,
                badgeText = "${viewModel.daysLeft} days left",
                badgeColor = IndigoPrimary,
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
                .background(Background)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Monthly Snapshot Card (Clean & Spacious)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable {
                            viewModel.validationError = null
                            monthlyLimitInput = viewModel.monthlyLimit.toInt().toString()
                            showMonthlyLimitDialog = true
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "MONTHLY SNAPSHOT",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Tap to Edit Upkeep ⚙️",
                                color = IndigoPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Total Upkeep", color = TextSecondary, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(3.dp))
                                Text("MK %,d".format(viewModel.monthlyLimit.toInt()), color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Spent", color = TextSecondary, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(viewModel.totalExpenses.replace("-", ""), color = ExpenseCoral, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Remaining", color = TextSecondary, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(viewModel.balance, color = IndigoPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Active Budgets Header & List (Clean, airy spacing)
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Active Budgets",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Tap any budget to modify its limit",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

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

            // Unbudgeted Categories Section
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Text(
                        text = "Available Categories",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Set limits for unbudgeted categories",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

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
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
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
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            TextButton(onClick = onSetLimitClick) {
                Text(
                    text = "+ Set Limit",
                    color = IndigoPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BudgetsScreenPreview() {
    BudgetsScreen()
}
