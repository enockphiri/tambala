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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.PhoneAndroid
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
import com.example.tambala.ui.components.AccountItem
import com.example.tambala.ui.components.BalanceCard
import com.example.tambala.ui.components.BottomNavBar
import com.example.tambala.ui.components.PrimaryButton
import com.example.tambala.ui.theme.GreenPrimary
import com.example.tambala.ui.theme.TextPrimary
import com.example.tambala.ui.theme.TextSecondary

@Composable
fun WalletsScreen(
    onAddAccountClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(selectedIndex = 1) // Wallets tab
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
                    Text(
                        text = "Wallets",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "73 days left",
                        color = GreenPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Total Balance Card
            item {
                BalanceCard(
                    balance = "MK 354,200",
                    income = "+MK 450,000",
                    expenses = "-MK 95,800"
                )
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Accounts Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Accounts",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "4 connected sources",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            // Account Items
            item {
                AccountItem(
                    name = "Cash in Hand",
                    subtitle = "Physical Cash",
                    balance = "MK 38,500",
                    icon = Icons.Default.AccountBalanceWallet,
                    iconBackground = Color(0xFF66BB6A),
                    badge = "Default"
                )
            }
            item {
                AccountItem(
                    name = "Airtel Money",
                    subtitle = "Mobile Money (099...)",
                    balance = "MK 125,700",
                    icon = Icons.Default.PhoneAndroid,
                    iconBackground = Color(0xFF42A5F5),
                    badge = "Instant"
                )
            }
            item {
                AccountItem(
                    name = "National Bank",
                    subtitle = "Student Visa (•••• 4892)",
                    balance = "MK 150,000",
                    icon = Icons.Default.AccountBalance,
                    iconBackground = Color(0xFF5C6BC0),
                    badge = "Linked"
                )
            }
            item {
                AccountItem(
                    name = "TNM Mpamba",
                    subtitle = "Mobile Money (088...)",
                    balance = "MK 40,000",
                    icon = Icons.Default.PhoneAndroid,
                    iconBackground = Color(0xFF26A69A),
                    badge = "Instant"
                )
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Add Account Button
            item {
                PrimaryButton(
                    text = "+ Add Account",
                    onClick = onAddAccountClick,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WalletsScreenPreview() {
    WalletsScreen()
}