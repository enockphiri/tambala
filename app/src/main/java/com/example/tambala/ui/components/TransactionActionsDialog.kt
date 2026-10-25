package com.example.tambala.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tambala.ui.theme.CardBorder
import com.example.tambala.ui.theme.IndigoPrimary
import com.example.tambala.ui.theme.TextPrimary
import com.example.tambala.ui.theme.TextSecondary

@Composable
fun TransactionActionsDialog(
    title: String,
    subtitle: String,
    amount: String,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Transaction Options", fontWeight = FontWeight.Bold, color = IndigoPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = BorderStroke(1.dp, CardBorder),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextPrimary)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = subtitle, fontSize = 12.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = amount, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = IndigoPrimary)
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onDelete) {
                    Text("Delete", color = Color.Red, fontWeight = FontWeight.Bold)
                }
                TextButton(onClick = onEdit) {
                    Text("Edit Transaction", fontWeight = FontWeight.Bold, color = IndigoPrimary)
                }
            }
        }
    )
}
