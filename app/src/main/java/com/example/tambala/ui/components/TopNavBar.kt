package com.example.tambala.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tambala.ui.theme.GreenPrimary
import com.example.tambala.ui.theme.TextPrimary
import com.example.tambala.ui.theme.TextSecondary

@Composable
fun TopNavBar(
    title: String,
    titleColor: Color = TextPrimary,
    subtitle: String? = null,
    badgeText: String? = null,
    badgeColor: Color = GreenPrimary,
    badgeBgColor: Color = GreenPrimary.copy(alpha = 0.1f)
) {
    Surface(
        shadowElevation = 3.dp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    color = titleColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            if (badgeText != null) {
                Surface(
                    color = badgeBgColor,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = badgeColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopNavBarPreview() {
    Column {
        TopNavBar(
            title = "Tambala",
            titleColor = GreenPrimary,
            subtitle = "Semester 1 (2024/2025)",
            badgeText = "73 days left"
        )
        Spacer(modifier = Modifier.height(10.dp))
        TopNavBar(
            title = "Budgets",
            subtitle = "November 2024"
        )
    }
}
