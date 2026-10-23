package com.example.tambala.ui.pages

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.PieChart
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.tambala.ui.components.TransactionItem
import com.example.tambala.ui.theme.Background
import com.example.tambala.ui.theme.CardBorder
import com.example.tambala.ui.theme.IndigoContainer
import com.example.tambala.ui.theme.IndigoPrimary
import com.example.tambala.ui.theme.TextPrimary
import com.example.tambala.ui.viewmodel.FinanceViewModel

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
    var showAddDialog by remember { mutableStateOf(false) }
    var isIncomeDialog by remember { mutableStateOf(false) }
    var titleInput by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(viewModel.categories.firstOrNull() ?: "Food & Dining") }

    var categoryExpanded by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    // Abrupt spending immediate popup dialog
    if (viewModel.abruptAlertMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.abruptAlertMessage = null },
            title = { Text("⚠️ Abrupt Spending Warning", fontWeight = FontWeight.Bold, color = Color(0xFFE11D48)) },
            text = { Text(viewModel.abruptAlertMessage ?: "") },
            confirmButton = {
                TextButton(onClick = { viewModel.abruptAlertMessage = null }) {
                    Text("OK", fontWeight = FontWeight.Bold, color = IndigoPrimary)
                }
            }
        )
    }

    // Notifications history dialog (opened by tapping the notification bell)
    if (showNotificationsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationsDialog = false },
            title = { Text("🔔 Notifications & Alerts", fontWeight = FontWeight.Bold, color = IndigoPrimary) },
            text = {
                LazyColumn(
                    modifier = Modifier.height(260.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.notificationsList) { note ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                            border = BorderStroke(1.dp, CardBorder),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = note,
                                modifier = Modifier.padding(12.dp),
                                fontSize = 13.sp,
                                color = TextPrimary
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationsDialog = false }) {
                    Text("Close", fontWeight = FontWeight.Bold, color = IndigoPrimary)
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
                            color = Color(0xFFE11D48),
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

                    // Only show Category Dropdown if it's an Expense (Income does not require expense category)
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
                            category = if (isIncomeDialog) "Income" else selectedCategory
                        )
                        if (success) {
                            titleInput = ""
                            amountInput = ""
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Save", fontWeight = FontWeight.Bold, color = IndigoPrimary)
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

    Scaffold(
        topBar = {
            TopNavBar(
                title = "Tambala",
                titleColor = IndigoPrimary,
                subtitle = viewModel.semesterName,
                badgeText = "${viewModel.daysLeft} days left",
                onProfileClick = onSettingsClick,
                onNotificationClick = { showNotificationsDialog = true }
            )
        },
        bottomBar = {
            BottomNavBar(selectedIndex = 0, onItemSelected = onNavigateToTab)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
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

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Quick Actions Section Header
            item {
                Text(
                    text = "Quick Actions",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(10.dp)) }

            // Primary Action Buttons (Coral for Log Expense, Teal for Add Income)
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

            item { Spacer(modifier = Modifier.height(12.dp)) }

            // Quick Shortcut Row
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp, horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        QuickActionShortcut(
                            title = "Set Budget",
                            icon = Icons.Default.PieChart,
                            onClick = onNavigateToBudgets
                        )
                        QuickActionShortcut(
                            title = "Categories",
                            icon = Icons.Default.Category,
                            onClick = onNavigateToCategories
                        )
                        QuickActionShortcut(
                            title = "Analytics",
                            icon = Icons.AutoMirrored.Filled.TrendingUp,
                            onClick = onNavigateToAnalysis
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

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
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "View All →",
                        color = IndigoPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigateToTransactions() }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Transactions (showing up to 5 recent transactions)
            items(viewModel.transactions.take(5)) { tx ->
                TransactionItem(
                    title = tx.title,
                    subtitle = tx.subtitle,
                    amount = tx.amount,
                    icon = tx.icon,
                    isIncome = tx.isIncome
                )
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun QuickActionShortcut(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = IndigoPrimary,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(IndigoContainer)
                .padding(10.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            color = TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}
