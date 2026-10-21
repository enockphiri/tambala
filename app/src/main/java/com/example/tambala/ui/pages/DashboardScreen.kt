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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
import com.example.tambala.ui.theme.GreenContainer
import com.example.tambala.ui.theme.GreenPrimary
import com.example.tambala.ui.theme.TextPrimary

@Composable
fun DashboardScreen(
    onLogExpenseClick: () -> Unit = {},
    onAddIncomeClick: () -> Unit = {},
    onNavigateToTab: (Int) -> Unit = {},
    onNavigateToBudgets: () -> Unit = {},
    onNavigateToCategories: () -> Unit = {},
    onNavigateToAnalysis: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopNavBar(
                title = "Tambala",
                titleColor = GreenPrimary,
                subtitle = "Semester 1 (2024/2025)",
                badgeText = "73 days left"
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
                    balance = "MK 354,200",
                    income = "+MK 450,000",
                    expenses = "-MK 95,800"
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

            // Primary Action Buttons (Red for Log Expense, Green for Add Income)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ExpenseButton(
                        text = "- Expense",
                        onClick = onLogExpenseClick,
                        modifier = Modifier.weight(1f)
                    )
                    IncomeButton(
                        text = "+ Add Income",
                        onClick = onAddIncomeClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            // Quick Shortcut Row - Clean Minimal Emerald Palette
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
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "View All →",
                        color = GreenPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
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
                    isIncome = false
                )
            }
            item {
                TransactionItem(
                    title = "Photocopying Past Exams",
                    subtitle = "Oct 15 • Printing • Cash",
                    amount = "-MK 300",
                    icon = Icons.Default.Print,
                    isIncome = false
                )
            }
            item {
                TransactionItem(
                    title = "2GB Campus Data Bundle",
                    subtitle = "Oct 14 • Airtime • Airtel",
                    amount = "-MK 2,000",
                    icon = Icons.Default.Wifi,
                    isIncome = false
                )
            }
            item {
                TransactionItem(
                    title = "HESLB Semester Allowance",
                    subtitle = "Oct 12 • Grant • Bank",
                    amount = "+MK 250,000",
                    icon = Icons.Default.AccountBalance,
                    isIncome = true
                )
            }
            item {
                TransactionItem(
                    title = "Sibling Upkeep Support",
                    subtitle = "Oct 10 • Allowance • Airtel",
                    amount = "+MK 50,000",
                    icon = Icons.Default.PhoneAndroid,
                    isIncome = true
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
            tint = GreenPrimary,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(GreenContainer)
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
