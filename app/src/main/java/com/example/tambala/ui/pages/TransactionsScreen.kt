package com.example.tambala.ui.pages

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tambala.ui.components.BottomNavBar
import com.example.tambala.ui.components.TopNavBar
import com.example.tambala.ui.components.TransactionActionsDialog
import com.example.tambala.ui.components.TransactionItem
import com.example.tambala.ui.viewmodel.FinanceViewModel
import com.example.tambala.ui.viewmodel.TransactionData

@Composable
fun TransactionsScreen(
    viewModel: FinanceViewModel = remember { FinanceViewModel() },
    onNavigateToTab: (Int) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTxForActions by remember { mutableStateOf<TransactionData?>(null) }
    var selectedTxToEdit by remember { mutableStateOf<TransactionData?>(null) }
    var editTitleInput by remember { mutableStateOf("") }
    var editAmountInput by remember { mutableStateOf("") }

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
            title = { Text("Edit Transaction", fontWeight = FontWeight.Bold) },
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
                        value = editTitleInput,
                        onValueChange = { editTitleInput = it },
                        label = { Text("Title / Note") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editAmountInput,
                        onValueChange = { editAmountInput = it },
                        label = { Text("Amount (MK) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val success = viewModel.editTransaction(
                            id = selectedTxToEdit!!.id,
                            newTitle = editTitleInput,
                            newAmountStr = editAmountInput
                        )
                        if (success) {
                            selectedTxToEdit = null
                        }
                    }
                ) {
                    Text("Save Changes", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.validationError = null
                        selectedTxToEdit = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    val filteredTransactions = viewModel.transactions.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopNavBar(
                title = "Transactions"
            )
        },
        bottomBar = {
            BottomNavBar(items = tambalaBottomNavItems, selectedIndex = 0, onItemSelected = onNavigateToTab)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search transactions...") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredTransactions, key = { it.id }) { tx ->
                    TransactionItem(
                        title = tx.title,
                        subtitle = tx.subtitle,
                        amount = tx.amount,
                        icon = tx.icon,
                        isIncome = tx.isIncome,
                        onClick = { selectedTxForActions = tx }
                    )
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TransactionsScreenPreview() {
    TransactionsScreen()
}

