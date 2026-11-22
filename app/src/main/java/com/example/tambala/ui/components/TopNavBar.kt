package com.example.tambala.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tambala.ui.theme.DividerColor
import com.example.tambala.ui.theme.ExpenseCoral
import com.example.tambala.ui.theme.IndigoContainer
import com.example.tambala.ui.theme.IndigoPrimary
import com.example.tambala.ui.theme.TextPrimary
import com.example.tambala.ui.theme.TextSecondary

@Composable
fun TopNavBar(
    title: String,
    titleColor: Color = TextPrimary,
    subtitle: String? = null,
    badgeText: String? = null,
    badgeColor: Color = IndigoPrimary,
    badgeBgColor: Color = IndigoContainer,
    userInitial: String = "S",
    hasUnreadNotifications: Boolean = true,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Surface(
        color = Color.White,
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User Profile Avatar Circle ("S") -> Tapping opens Settings
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(IndigoContainer)
                        .clickable { onProfileClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userInitial,
                        color = IndigoPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Page Title + Subtitle
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        color = titleColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (subtitle != null) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = subtitle,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Right Badge / Status Pill
                if (badgeText != null) {
                    Surface(
                        color = badgeBgColor,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = badgeText,
                            color = badgeColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                // Notification Bell Icon Button with dynamic unread indicator dot
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9))
                        .clickable { onNotificationClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    if (hasUnreadNotifications) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .align(Alignment.TopEnd)
                                .padding(end = 2.dp, top = 2.dp)
                                .clip(CircleShape)
                                .background(ExpenseCoral)
                        )
                    }
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = DividerColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopNavBarPreview() {
    Column {
        TopNavBar(
            title = "Tambala",
            titleColor = IndigoPrimary,
            subtitle = "Semester 1 (2024/2025)",
            badgeText = "73 days left"
        )
    }
}
