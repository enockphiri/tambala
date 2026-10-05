package com.example.tambala.ui.pages

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.tambala.ui.components.BottomNavBar
import com.example.tambala.ui.components.BudgetProgressItem
import com.example.tambala.ui.components.TopNavBar
import com.example.tambala.ui.theme.Background
import com.example.tambala.ui.theme.CardBorder
import com.example.tambala.ui.theme.ExpenseRed
import com.example.tambala.ui.theme.GreenPrimary
import com.example.tambala.ui.theme.TextPrimary
import com.example.tambala.ui.theme.TextSecondary

@Composable
fun BudgetsScreen() {
    Scaffold(
        topBar = {
            TopNavBar(
                title = "Budgets",
                subtitle = "November 2024",
                badgeText = "Active Term",
                badgeColor = GreenPrimary
            )
        },
        bottomBar = {
            BottomNavBar(selectedIndex = 1) // Budgets tab
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(innerPadding)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Monthly Snapshot Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "MONTHLY SNAPSHOT",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Allocated", color = TextSecondary, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("MK 120,000", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Spent", color = TextSecondary, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("MK 45,200", color = ExpenseRed, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Remaining", color = TextSecondary, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("MK 74,800", color = GreenPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Active Budget Items
            item {
                BudgetProgressItem(
                    title = "Food & Groceries",
                    spent = "MK 32,000",
                    limit = "MK 60,000",
                    remaining = "MK 28,000 left",
                    progress = 0.53f,
                    icon = Icons.Default.Restaurant,
                    iconBackground = Color(0xFFFF9800),
                    targetPerDay = "MK 2,000/day"
                )
            }
            item {
                BudgetProgressItem(
                    title = "Campus Transport",
                    spent = "MK 13,200",
                    limit = "MK 25,000",
                    remaining = "MK 11,800 left",
                    progress = 0.72f,
                    icon = Icons.Default.DirectionsBus,
                    iconBackground = Color(0xFF42A5F5),
                    targetPerDay = "MK 830/day"
                )
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Unbudgeted Categories Header
            item {
                Text(
                    text = "Unbudgeted Categories",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Simple unbudgeted items
            item {
                UnbudgetedItem(title = "Academic Printing", iconBg = Color(0xFF42A5F5))
            }
            item {
                UnbudgetedItem(title = "Airtime & Data", iconBg = Color(0xFF26A69A))
            }
            item {
                UnbudgetedItem(title = "Rent & Hostel", iconBg = Color(0xFF5C6BC0))
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun UnbudgetedItem(
    title: String,
    iconBg: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder),
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
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "+ Set Limit",
                color = GreenPrimary,
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
