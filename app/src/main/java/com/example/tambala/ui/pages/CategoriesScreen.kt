package com.example.tambala.ui.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import com.example.tambala.ui.components.BottomNavBar
import com.example.tambala.ui.components.PrimaryButton
import com.example.tambala.ui.components.TopNavBar
import com.example.tambala.ui.theme.Background
import com.example.tambala.ui.theme.CardBorder
import com.example.tambala.ui.theme.TextPrimary
import com.example.tambala.ui.theme.TextSecondary

@Composable
fun CategoriesScreen() {
    Scaffold(
        topBar = {
            TopNavBar(
                title = "Categories",
                subtitle = "Organize your campus budget flow"
            )
        },
        bottomBar = {
            BottomNavBar(selectedIndex = 2)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(innerPadding)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Add Category Button
            item {
                PrimaryButton(
                    text = "+ Add Category",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Section Title
            item {
                Text(
                    text = "ACTIVE OUTFLOWS",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(10.dp)) }

            // Categories
            item {
                CategoryItem("Food & Dining", "48 campus logs this term", Icons.Default.Restaurant, Color(0xFFFF9800))
            }
            item {
                CategoryItem("Rent & Hostel", "Monthly fixed allocation", Icons.Default.Home, Color(0xFF5C6BC0))
            }
            item {
                CategoryItem("Assignment & Printing", "19 print center visits", Icons.Default.Print, Color(0xFF26A69A))
            }
            item {
                CategoryItem("Transport", "34 minibus rides logged", Icons.Default.DirectionsBus, Color(0xFF42A5F5))
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun CategoryItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(iconBg)
                    .padding(9.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = TextSecondary
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CategoriesScreenPreview() {
    CategoriesScreen()
}
