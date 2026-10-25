package com.example.tambala.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TransactionItem(
    title: String,
    subtitle: String,
    amount: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isIncome: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val amountColor = if (isIncome) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
    val iconContainer = if (isIncome) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.errorContainer
    val cardModifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 5.dp)
        .then(
            if (onClick != null) Modifier.clickable(onClick = onClick, role = Role.Button) else Modifier
        )

    Card(
        modifier = cardModifier,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(modifier = Modifier.size(44.dp), shape = CircleShape, color = iconContainer) {
                androidx.compose.material3.Icon(icon, contentDescription = null, modifier = Modifier.padding(11.dp), tint = amountColor)
            }
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(amount, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = amountColor)
            }
        }
    }
}
