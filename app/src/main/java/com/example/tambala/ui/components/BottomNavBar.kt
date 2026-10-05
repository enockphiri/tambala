package com.example.tambala.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tambala.ui.theme.GreenPrimary
import com.example.tambala.ui.theme.NavUnselected

// Data class for each navigation item
data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun BottomNavBar(
    selectedIndex: Int = 0,
    onItemSelected: (Int) -> Unit = {}
) {
    val items = listOf(
        BottomNavItem("Dashboard", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem("Budgets", Icons.Filled.PieChart, Icons.Outlined.PieChart),
        BottomNavItem("Categories", Icons.Filled.Category, Icons.Outlined.Category),
        BottomNavItem("Analysis", Icons.Filled.TrendingUp, Icons.Outlined.TrendingUp)
    )

    Surface(
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedIndex == index,
                    onClick = { onItemSelected(index) },
                    icon = {
                        Icon(
                            imageVector = if (selectedIndex == index) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    },
                    label = {
                        Text(text = item.title)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GreenPrimary,
                        selectedTextColor = GreenPrimary,
                        unselectedIconColor = NavUnselected,
                        unselectedTextColor = NavUnselected,
                        indicatorColor = GreenPrimary.copy(alpha = 0.12f)
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    BottomNavBar(selectedIndex = 0)
}
