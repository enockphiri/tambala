package com.example.tambala.ui.pages

import androidx.compose.material3.MaterialTheme

import android.app.DatePickerDialog
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
import com.example.tambala.ui.viewmodel.FinanceViewModel
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
        DatePickerDialog(
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
        ).show()
    }

    Scaffold(
        topBar = {
            TopNavBar(
                title = "Settings"
            )
        },
        bottomBar = {
            BottomNavBar(items = tambalaBottomNavItems, selectedIndex = 0, onItemSelected = onNavigateToTab)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Semester Configuration Card (Options 1 & 2, not written/hardcoded)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Semester Configuration",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Select Semester 1 or Semester 2 via options (no hardcoded year)
                        Column {
                            Text("Select Semester Option", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box {
                                OutlinedButton(
                                    onClick = { semesterDropdownExpanded = true },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Semester ${viewModel.activeSemesterNumber}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
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

                        // Start Date Picker Field linked to active semester dates
                        Column {
                            Text("Semester ${viewModel.activeSemesterNumber} Start Date", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
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
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                    disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        // End Date Picker Field linked to active semester dates
                        Column {
                            Text("Semester ${viewModel.activeSemesterNumber} End Date", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)
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
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                    disabledBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        // Calculated Days Remaining display
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Days remaining", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                            Text("${viewModel.daysLeft}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
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
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Abrupt Spending Alerts",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Enable Abrupt Spending Alerts", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                                Text("Notify if an expense exceeds threshold", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
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

