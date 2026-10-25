package com.example.tambala.ui.pages

import androidx.compose.material3.MaterialTheme

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tambala.ui.components.BalanceCard
import com.example.tambala.ui.components.BottomNavBar
import com.example.tambala.ui.components.ExpenseButton
import com.example.tambala.ui.components.IncomeButton
import com.example.tambala.ui.components.TopNavBar
import com.example.tambala.ui.components.TransactionActionsDialog
import com.example.tambala.ui.components.TransactionItem
import com.example.tambala.ui.viewmodel.FinanceViewModel
import com.example.tambala.ui.viewmodel.TransactionData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: FinanceViewModel = remember { FinanceViewModel() },
    onNavigateToTab: (Int) -> Unit = {},
    onNavigateToBudgets: () -> Unit = {},
    onNavigateToCategories: () -> Unit = {},
    onNavigateToAnalysis: () -> Unit = {},
    onNavigateToTransactions: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var isIncomeDialog by remember { mutableStateOf(false) }
    var titleInput by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(viewModel.categories.firstOrNull() ?: "Food & Dining") }

    var txDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var txDateStr by remember { mutableStateOf(SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(System.currentTimeMillis())) }

    var categoryExpanded by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var selectedTxForActions by remember { mutableStateOf<TransactionData?>(null) }
    var selectedTxToEdit by remember { mutableStateOf<TransactionData?>(null) }
    var editTitleInput by remember { mutableStateOf("") }
    var editAmountInput by remember { mutableStateOf("") }

    fun showAddTxDatePicker() {
        val cal = Calendar.getInstance().apply { timeInMillis = txDateMillis }
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedCal = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                txDateMillis = selectedCal.timeInMillis
                txDateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(selectedCal.time)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Abrupt spending immediate popup dialog
    if (viewModel.abruptAlertMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.abruptAlertMessage = null },
            title = { Text("⚠️ Abrupt Spending Warning", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error) },
            text = { Text(viewModel.abruptAlertMessage ?: "") },
            confirmButton = {
                TextButton(onClick = { viewModel.abruptAlertMessage = null }) {
                    Text("OK", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    // Notifications history dialog (opened by tapping the notification bell)
    if (showNotificationsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationsDialog = false },
            title = { Text("🔔 Notifications & Alerts", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
            text = {
                LazyColumn(
                    modifier = Modifier.height(260.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.notificationsList) { note ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = note,
                                modifier = Modifier.padding(12.dp),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationsDialog = false }) {
                    Text("Close", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.validationError = null
                showAddDialog = false
            },
            title = {
                Text(
                    text = if (isIncomeDialog) "Record Income" else "Log Expense",
                    fontWeight = FontWeight.Bold
                )
            },
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
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text(if (isIncomeDialog) "Income Source (e.g. Allowance)" else "Title / Note (e.g. Cafeteria)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = amountInput,
                        onValueChange = { amountInput = it },
                        label = { Text("Amount (MK) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Date Selection Field
                    Column {
                        Text("Transaction Date:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = txDateStr,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showAddTxDatePicker() },
                            enabled = false,
                            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    // Only show Category Dropdown if it's an Expense
                    if (!isIncomeDialog) {
                        Text("Select Category:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Box {
                            OutlinedButton(
                                onClick = { categoryExpanded = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(selectedCategory)
                            }
                            DropdownMenu(
                                expanded = categoryExpanded,
                                onDismissRequest = { categoryExpanded = false }
                            ) {
                                viewModel.categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat) },
                                        onClick = {
                                            selectedCategory = cat
                                            categoryExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val success = viewModel.addTransaction(
                            title = titleInput,
                            amountStr = amountInput,
                            isIncome = isIncomeDialog,
                            category = if (isIncomeDialog) "Income" else selectedCategory,
                            dateMillis = txDateMillis,
                            dateStr = txDateStr
                        )
                        if (success) {
                            titleInput = ""
                            amountInput = ""
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Save", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.validationError = null
                    showAddDialog = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    selectedTxForActions?.let { transaction ->
        TransactionActionsDialog(
            title = transaction.title,
            subtitle = transaction.subtitle,
            amount = transaction.amount,
            onDismiss = { selectedTxForActions = null },
            onEdit = {
                viewModel.validationError = null
                editTitleInput = transaction.title
                editAmountInput = transaction.rawAmount.toInt().toString()
                selectedTxForActions = null
                selectedTxToEdit = transaction
            },
            onDelete = {
                viewModel.deleteTransaction(transaction.id)
                selectedTxForActions = null
            }
        )
    }

    if (selectedTxToEdit != null) {
        AlertDialog(
            onDismissRequest = {
                viewModel.validationError = null
                selectedTxToEdit = null
            },
            title = { Text("Edit transaction", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (viewModel.validationError != null) {
                        Text(
                            text = viewModel.validationError.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    OutlinedTextField(
                        value = editTitleInput,
                        onValueChange = { editTitleInput = it },
                        label = { Text("Title / note") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editAmountInput,
                        onValueChange = { editAmountInput = it },
                        label = { Text("Amount (MK)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val transaction = selectedTxToEdit ?: return@TextButton
                    if (viewModel.editTransaction(
                            id = transaction.id,
                            newTitle = editTitleInput,
                            newAmountStr = editAmountInput
                        )
                    ) {
                        selectedTxToEdit = null
                    }
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.validationError = null
                    selectedTxToEdit = null
                }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopNavBar(
                title = "Tambala",
                userInitial = "S",
                subtitle = viewModel.semesterName,
                badgeText = "${viewModel.daysLeft} days left",
                onProfileClick = onSettingsClick,
                onNotificationClick = { showNotificationsDialog = true }
            )
        },
        bottomBar = {
            BottomNavBar(items = tambalaBottomNavItems, selectedIndex = 0, onItemSelected = onNavigateToTab)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Balance Card
            item {
                BalanceCard(
                    balance = viewModel.balance,
                    income = viewModel.totalIncome,
                    expenses = viewModel.totalExpenses
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
            // Primary actions
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ExpenseButton(
                        text = "- Log Expense",
                        onClick = {
                            viewModel.validationError = null
                            isIncomeDialog = false
                            showAddDialog = true
                        },
                        modifier = Modifier.weight(1f)
                    )
                    IncomeButton(
                        text = "+ Add Income",
                        onClick = {
                            viewModel.validationError = null
                            isIncomeDialog = true
                            showAddDialog = true
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Recent Activity Header with View All working navigation
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Activity",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "View All →",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigateToTransactions() }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // A short list keeps the dashboard focused. Open an item for edit/delete actions.
            items(viewModel.transactions.take(3), key = { it.id }) { tx ->
                TransactionItem(
                    title = tx.title,
                    subtitle = tx.subtitle,
                    amount = tx.amount,
                    icon = tx.icon,
                    isIncome = tx.isIncome,
                    onClick = { selectedTxForActions = tx }
                )
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}




