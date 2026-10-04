package com.example.tambala.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tambala.ui.components.BalanceCard
import com.example.tambala.ui.components.BottomNavBar
import com.example.tambala.ui.components.PrimaryButton
import com.example.tambala.ui.components.SecondaryButton
import com.example.tambala.ui.components.TransactionItem
import com.example.tambala.ui.theme.GreenPrimary
import com.example.tambala.ui.theme.TextPrimary
import com.example.tambala.ui.theme.TextSecondary

@Composable
fun DashboardScreen(
    onLogExpenseClick: () -> Unit = {},
    onAddIncomeClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(selectedIndex = 0)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(innerPadding)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Tambala",
                            color = GreenPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Semester 1 (2024/2025)",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }

                    Text(
                        text = "73 days left",
                        color = GreenPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Balance Card
            item {
                BalanceCard(
                    balance = "MK 354,200",
                    income = "+MK 450,000",
                    expenses = "-MK 95,800"
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Buttons
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PrimaryButton(
                        text = "+ Log Cash Spent",
                        onClick = onLogExpenseClick,
                        modifier = Modifier.weight(1f)
                    )
                    SecondaryButton(
                        text = "Add Income",
                        onClick = onAddIncomeClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Recent Activity Header
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
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "View All →",
                        color = GreenPrimary,
                        fontSize = 13.sp
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Transactions
            item {
                TransactionItem(
                    title = "Campus Cafeteria Lunch",
                    subtitle = "Oct 15 • Food • Cash",
                    amount = "-MK 1,800",
                    icon = Icons.Default.Restaurant,
                    iconBackground = Color(0xFFFF9800),
                    isIncome = false
                )
            }
            item {
                TransactionItem(
                    title = "Photocopying Past Exams",
                    subtitle = "Oct 15 • Printing • Cash",
                    amount = "-MK 300",
                    icon = Icons.Default.Print,
                    iconBackground = Color(0xFF42A5F5),
                    isIncome = false
                )
            }
            item {
                TransactionItem(
                    title = "2GB Campus Data Bundle",
                    subtitle = "Oct 14 • Airtime • Airtel",
                    amount = "-MK 2,000",
                    icon = Icons.Default.Wifi,
                    iconBackground = Color(0xFF26A69A),
                    isIncome = false
                )
            }
            item {
                TransactionItem(
                    title = "HESLB Semester Allowance",
                    subtitle = "Oct 12 • Grant • Bank",
                    amount = "+MK 250,000",
                    icon = Icons.Default.AccountBalance,
                    iconBackground = Color(0xFF66BB6A),
                    isIncome = true
                )
            }
            item {
                TransactionItem(
                    title = "Sibling Upkeep Support",
                    subtitle = "Oct 10 • Allowance • Airtel",
                    amount = "+MK 50,000",
                    icon = Icons.Default.PhoneAndroid,
                    iconBackground = Color(0xFFAB47BC),
                    isIncome = true
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