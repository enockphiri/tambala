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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.tambala.ui.components.BottomNavBar
import com.example.tambala.ui.theme.GreenPrimary
import com.example.tambala.ui.theme.TextPrimary
import com.example.tambala.ui.theme.TextSecondary

@Composable
fun AnalysisScreen() {
    Scaffold(
        bottomBar = {
            BottomNavBar(selectedIndex = 4)
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
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Analysis",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Expense Overview",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
            }

            // Summary Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "TOTAL ANALYZED",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "MK 95,800 spent",
                            color = TextPrimary,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Semester Inflow", color = TextSecondary, fontSize = 12.sp)
                                Text(
                                    "+MK 450,000",
                                    color = GreenPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Column {
                                Text("Net Remaining", color = TextSecondary, fontSize = 12.sp)
                                Text(
                                    "MK 354,200",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Category Breakdown Header
            item {
                Text(
                    text = "Category Breakdown",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(10.dp)) }

            // Breakdown Items
            item { BreakdownItem("Food & Dining", "36%", "MK 34,500", Color(0xFFFF9800)) }
            item { BreakdownItem("Rent & Hostel", "28%", "MK 26,800", Color(0xFF5C6BC0)) }
            item { BreakdownItem("Transport", "18%", "MK 17,200", Color(0xFF42A5F5)) }
            item { BreakdownItem("Academic Printing", "11%", "MK 10,500", Color(0xFF26A69A)) }
            item { BreakdownItem("Airtime & Others", "7%", "MK 6,800", Color(0xFFAB47BC)) }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun BreakdownItem(
    title: String,
    percentage: String,
    amount: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color indicator
            Spacer(
                modifier = Modifier
                    .size(12.dp)
                    .background(color, shape = RoundedCornerShape(50))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = percentage,
                color = TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(end = 12.dp)
            )

            Text(
                text = amount,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AnalysisScreenPreview() {
    AnalysisScreen()
}