package com.example.tambala.ui.pages

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tambala.ui.components.BottomNavBar
import com.example.tambala.ui.components.TopNavBar
import com.example.tambala.ui.viewmodel.FinanceViewModel

@Composable
fun AnalysisScreen(
    viewModel: FinanceViewModel = remember { FinanceViewModel() },
    onNavigateToTab: (Int) -> Unit = {}
) {
    val filters = listOf("Day", "Week", "Month", "Semester")
    val spendingReports = viewModel.getSpendingByCategory()
    val incomeReports = viewModel.getIncomeBySource()

    var monthDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopNavBar(
                title = "Analysis"
            )
        },
        bottomBar = {
            BottomNavBar(items = tambalaBottomNavItems, selectedIndex = 3, onItemSelected = onNavigateToTab)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Period Filter Chips (Day, Week, Month, Semester)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    filters.forEach { filter ->
                        FilterChip(
                            selected = viewModel.analysisFilter == filter,
                            onClick = { viewModel.updateAnalysisFilter(filter) },
                            label = {
                                Text(
                                    text = filter,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // If "Month" filter is selected, show Jan-Dec Month Selector Dropdown
            if (viewModel.analysisFilter == "Month") {
                item { Spacer(modifier = Modifier.height(12.dp)) }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Select Calendar Month (Jan - Dec):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box {
                            OutlinedButton(
                                onClick = { monthDropdownExpanded = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("📅 Month: ${viewModel.selectedReportMonth}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                            DropdownMenu(
                                expanded = monthDropdownExpanded,
                                onDismissRequest = { monthDropdownExpanded = false }
                            ) {
                                viewModel.availableMonths.forEach { m ->
                                    DropdownMenuItem(
                                        text = { Text(m, fontWeight = if (viewModel.selectedReportMonth == m) FontWeight.Bold else FontWeight.Normal) },
                                        onClick = {
                                            viewModel.updateSelectedReportMonth(m)
                                            monthDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Summary Card with dynamic values
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        val headerLabel = if (viewModel.analysisFilter == "Month") "TOTAL ANALYZED (${viewModel.selectedReportMonth.uppercase()})" else "TOTAL ANALYZED (${viewModel.analysisFilter.uppercase()})"
                        Text(
                            text = headerLabel,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "MK %,d spent".format(viewModel.filteredSpentRaw.toInt()),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Total Inflow", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "+MK %,d".format(viewModel.filteredInflowRaw.toInt()),
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Text("Net Balance", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = viewModel.balance,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Spending by Category Report Header
            item {
                Text(
                    text = if (viewModel.analysisFilter == "Month") "Spending by Category (${viewModel.selectedReportMonth})" else "Spending by Category (${viewModel.analysisFilter})",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            items(spendingReports) { rep ->
                ReportCardItem(title = rep.title, percentage = rep.percentage, amount = rep.amount, isIncome = false)
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Income by Source Report Header
            item {
                Text(
                    text = if (viewModel.analysisFilter == "Month") "Income by Source (${viewModel.selectedReportMonth})" else "Income by Source (${viewModel.analysisFilter})",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            items(incomeReports) { rep ->
                ReportCardItem(title = rep.title, percentage = rep.percentage, amount = rep.amount, isIncome = true)
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun ReportCardItem(
    title: String,
    percentage: String,
    amount: String,
    isIncome: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .size(10.dp)
                    .background(if (isIncome) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(50))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = percentage,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
                modifier = Modifier.padding(end = 12.dp)
            )

            Text(
                text = amount,
                color = if (isIncome) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AnalysisScreenPreview() {
    AnalysisScreen()
}

