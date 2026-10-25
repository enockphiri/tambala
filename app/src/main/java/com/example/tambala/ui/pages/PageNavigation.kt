package com.example.tambala.ui.pages

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.TrendingUp
import com.example.tambala.ui.components.BottomNavItem

internal val tambalaBottomNavItems = listOf(
    BottomNavItem("Dashboard", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem("Budgets", Icons.Filled.PieChart, Icons.Outlined.PieChart),
    BottomNavItem("Categories", Icons.Filled.Category, Icons.Outlined.Category),
    BottomNavItem("Analysis", Icons.Filled.TrendingUp, Icons.Outlined.TrendingUp)
)
