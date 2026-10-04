package com.example.tambala.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.tambala.ui.theme.GreenMain
import com.example.tambala.ui.theme.GreenPrimary
import com.example.tambala.ui.theme.TextPrimary
import com.example.tambala.ui.theme.TextSecondary

@Composable
fun BudgetProgressItem(
    title: String,
    spent: String,
    limit: String,
    remaining: String,
    progress: Float,          // value between 0f and 1f (e.g. 0.53f = 53%)
    icon: ImageVector,
    iconBackground: Color,
    targetPerDay: String = ""
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Top Row: Icon + Title + Remaining
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icon
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(iconBackground)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Title + Spent/Limit
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "$spent of $limit",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                // Remaining amount
                Text(
                    text = remaining,
                    color = GreenPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar (Built-in)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = GreenMain,
                trackColor = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Row: Percentage + Target per day
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${(progress * 100).toInt()}% used",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                if (targetPerDay.isNotEmpty()) {
                    Text(
                        text = "Target: $targetPerDay",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetProgressItemPreview() {
    Column {
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

        BudgetProgressItem(
            title = "Campus Transport",
            spent = "MK 13,200",
            limit = "MK 25,000",
            remaining = "MK 11,800 left",
            progress = 0.52f,
            icon = Icons.Default.DirectionsBus,
            iconBackground = Color(0xFF42A5F5),
            targetPerDay = "MK 830/day"
        )
    }
}