package com.example.tambala.ui.pages

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tambala.ui.components.BottomNavBar
import com.example.tambala.ui.components.TopNavBar
import com.example.tambala.ui.theme.Background
import com.example.tambala.ui.theme.CardBorder
import com.example.tambala.ui.theme.IndigoPrimary
import com.example.tambala.ui.theme.TextPrimary
import com.example.tambala.ui.theme.TextSecondary
import com.example.tambala.ui.viewmodel.FinanceViewModel
import com.example.tambala.worker.TambalaWorkScheduler
import java.util.Calendar

@Composable
fun SettingsScreen(
    viewModel: FinanceViewModel = remember { FinanceViewModel() },
    onNavigateToTab: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    var semesterDropdownExpanded by remember { mutableStateOf(false) }

    fun showDatePicker(initialMillis: Long, onSelected: (Long) -> Unit) {
        val cal = Calendar.getInstance().apply { timeInMillis = initialMillis }
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedCal = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                onSelected(selectedCal.timeInMillis)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    Scaffold(
        topBar = {
            TopNavBar(
                title = "Settings",
                subtitle = "Semesters & background features"
            )
        },
        bottomBar = {
            BottomNavBar(items = tambalaBottomNavItems, selectedIndex = 0, onItemSelected = onNavigateToTab)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(innerPadding)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Semester Configuration Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorder),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Semester Configuration",
                            color = IndigoPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Select Semester 1 or Semester 2
                        Column {
                            Text("Select Semester Option", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box {
                                OutlinedButton(
                                    onClick = { semesterDropdownExpanded = true },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("🎓 Semester ${viewModel.activeSemesterNumber}", fontWeight = FontWeight.Bold, color = IndigoPrimary)
                                }
                                DropdownMenu(
                                    expanded = semesterDropdownExpanded,
                                    onDismissRequest = { semesterDropdownExpanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Semester 1") },
                                        onClick = {
                                            viewModel.activeSemesterNumber = 1
                                            semesterDropdownExpanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Semester 2") },
                                        onClick = {
                                            viewModel.activeSemesterNumber = 2
                                            semesterDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Start Date Picker
                        Column {
                            Text("Semester ${viewModel.activeSemesterNumber} Start Date", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            val currentStartMillis = if (viewModel.activeSemesterNumber == 1) viewModel.sem1StartMillis else viewModel.sem2StartMillis
                            val currentStartStr = viewModel.activeStartStr
                            OutlinedTextField(
                                value = currentStartStr,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showDatePicker(currentStartMillis) { millis ->
                                            viewModel.updateStartDate(millis)
                                        }
                                    },
                                enabled = false,
                                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = TextPrimary,
                                    disabledBorderColor = CardBorder,
                                    disabledLabelColor = TextSecondary
                                )
                            )
                        }

                        // End Date Picker
                        Column {
                            Text("Semester ${viewModel.activeSemesterNumber} End Date", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            val currentEndMillis = if (viewModel.activeSemesterNumber == 1) viewModel.sem1EndMillis else viewModel.sem2EndMillis
                            val currentEndStr = viewModel.activeEndStr
                            OutlinedTextField(
                                value = currentEndStr,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showDatePicker(currentEndMillis) { millis ->
                                            viewModel.updateEndDate(millis)
                                        }
                                    },
                                enabled = false,
                                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = TextPrimary,
                                    disabledBorderColor = CardBorder,
                                    disabledLabelColor = TextSecondary
                                )
                            )
                        }

                        // Calculated Days Remaining
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Calculated Days Remaining:", fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                Text("${viewModel.daysLeft} days", fontWeight = FontWeight.Bold, color = IndigoPrimary, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Background Features & WorkManager Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorder),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Manager",
                            color = IndigoPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // 1. Daily Expense Reminder
                        var reminderEnabled by remember {
                            mutableStateOf(context.getSharedPreferences("tambala_prefs", Context.MODE_PRIVATE).getBoolean("daily_reminder_enabled", true))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Daily Expense Reminder", color = TextPrimary, fontWeight = FontWeight.Medium)
                                Text("Remind to log upkeep daily", color = TextSecondary, fontSize = 12.sp)
                            }
                            Switch(
                                checked = reminderEnabled,
                                onCheckedChange = {
                                    reminderEnabled = it
                                    context.getSharedPreferences("tambala_prefs", Context.MODE_PRIVATE).edit().putBoolean("daily_reminder_enabled", it).apply()
                                    TambalaWorkScheduler.scheduleDailyReminder(context, it)
                                }
                            )
                        }

                        // 2. Low-Balance Monitoring
                        var lowBalEnabled by remember {
                            mutableStateOf(context.getSharedPreferences("tambala_prefs", Context.MODE_PRIVATE).getBoolean("low_balance_enabled", true))
                        }
                        var lowBalThreshold by remember {
                            mutableStateOf(context.getSharedPreferences("tambala_prefs", Context.MODE_PRIVATE).getFloat("low_balance_threshold", 10000f).toInt().toString())
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Low-Balance Monitoring", color = TextPrimary, fontWeight = FontWeight.Medium)
                                Text("Warn when balance drops below threshold", color = TextSecondary, fontSize = 12.sp)
                            }
                            Switch(
                                checked = lowBalEnabled,
                                onCheckedChange = {
                                    lowBalEnabled = it
                                    context.getSharedPreferences("tambala_prefs", Context.MODE_PRIVATE).edit().putBoolean("low_balance_enabled", it).apply()
                                    TambalaWorkScheduler.scheduleLowBalanceMonitoring(context, it)
                                }
                            )
                        }

                        if (lowBalEnabled) {
                            OutlinedTextField(
                                value = lowBalThreshold,
                                onValueChange = {
                                    lowBalThreshold = it
                                    val f = it.toFloatOrNull() ?: 10000f
                                    context.getSharedPreferences("tambala_prefs", Context.MODE_PRIVATE).edit().putFloat("low_balance_threshold", f).apply()
                                },
                                label = { Text("Low-Balance Threshold (MK)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // 3. Automatic Local Backup
                        var backupEnabled by remember {
                            mutableStateOf(context.getSharedPreferences("tambala_prefs", Context.MODE_PRIVATE).getBoolean("auto_backup_enabled", true))
                        }
                        var isDailyBackup by remember {
                            mutableStateOf(context.getSharedPreferences("tambala_prefs", Context.MODE_PRIVATE).getBoolean("auto_backup_daily", true))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Automatic Local Backup", color = TextPrimary, fontWeight = FontWeight.Medium)
                                Text("Back up financial data offline locally", color = TextSecondary, fontSize = 12.sp)
                            }
                            Switch(
                                checked = backupEnabled,
                                onCheckedChange = {
                                    backupEnabled = it
                                    context.getSharedPreferences("tambala_prefs", Context.MODE_PRIVATE).edit().putBoolean("auto_backup_enabled", it).apply()
                                    TambalaWorkScheduler.scheduleAutoBackup(context, it, isDailyBackup)
                                }
                            )
                        }

                        if (backupEnabled) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Backup Frequency", color = TextPrimary, fontSize = 14.sp)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedButton(
                                        onClick = {
                                            isDailyBackup = true
                                            context.getSharedPreferences("tambala_prefs", Context.MODE_PRIVATE).edit().putBoolean("auto_backup_daily", true).apply()
                                            TambalaWorkScheduler.scheduleAutoBackup(context, true, true)
                                        }
                                    ) {
                                        Text("Daily", color = if (isDailyBackup) IndigoPrimary else TextSecondary)
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            isDailyBackup = false
                                            context.getSharedPreferences("tambala_prefs", Context.MODE_PRIVATE).edit().putBoolean("auto_backup_daily", false).apply()
                                            TambalaWorkScheduler.scheduleAutoBackup(context, true, false)
                                        }
                                    ) {
                                        Text("Weekly", color = if (!isDailyBackup) IndigoPrimary else TextSecondary)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Spending & Notifications Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorder),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Abrupt Spending Alerts",
                            color = IndigoPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Enable Abrupt Spending Alerts", color = TextPrimary, fontWeight = FontWeight.Medium)
                                Text("Notify if an expense exceeds threshold", color = TextSecondary, fontSize = 12.sp)
                            }
                            Switch(
                                checked = viewModel.abruptNotificationEnabled,
                                onCheckedChange = { viewModel.abruptNotificationEnabled = it }
                            )
                        }

                        if (viewModel.abruptNotificationEnabled) {
                            OutlinedTextField(
                                value = viewModel.abruptThreshold.toInt().toString(),
                                onValueChange = { viewModel.abruptThreshold = it.toDoubleOrNull() ?: viewModel.abruptThreshold },
                                label = { Text("Alert Threshold (MK)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
