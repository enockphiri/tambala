package com.example.tambala.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
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
import com.example.tambala.ui.theme.ExpenseRed
import com.example.tambala.ui.theme.IncomeGreen
import com.example.tambala.ui.theme.TextPrimary
import com.example.tambala.ui.theme.TextSecondary

@Composable
fun TransactionItem(
    title: String,
    subtitle: String,
    amount: String,
    icon: ImageVector,
    iconBackground: Color,
    isIncome: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Circle
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(iconBackground)
                .padding(9.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        // Title + Subtitle
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.size(3.dp))
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        // Amount
        Text(
            text = amount,
            color = if (isIncome) IncomeGreen else ExpenseRed,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemPreview() {
    Column {
        TransactionItem(
            title = "Campus Cafeteria Lunch",
            subtitle = "Oct 15 • Food • Cash",
            amount = "-MK 1,800",
            icon = Icons.Default.Restaurant,
            iconBackground = Color(0xFFFF9800),
            isIncome = false
        )

        TransactionItem(
            title = "Photocopying Past Exams",
            subtitle = "Oct 15 • Printing • Cash",
            amount = "-MK 300",
            icon = Icons.Default.Print,
            iconBackground = Color(0xFF42A5F5),
            isIncome = false
        )

        TransactionItem(
            title = "2GB Campus Data Bundle",
            subtitle = "Oct 14 • Airtime • Airtel",
            amount = "-MK 2,000",
            icon = Icons.Default.Wifi,
            iconBackground = Color(0xFF26A69A),
            isIncome = false
        )

        TransactionItem(
            title = "HESLB Semester Allowance",
            subtitle = "Oct 12 • Grant • Bank",
            amount = "+MK 250,000",
            icon = Icons.Default.AccountBalance,
            iconBackground = Color(0xFF66BB6A),
            isIncome = true
        )

        TransactionItem(
            title = "Sibling Upkeep Support",
            subtitle = "Oct 10 • Allowance • Airtel",
            amount = "+MK 50,000",
            icon = Icons.Default.PhoneAndroid,
            iconBackground = Color(0xFFAB47BC),
            isIncome = true
        )

    }
}