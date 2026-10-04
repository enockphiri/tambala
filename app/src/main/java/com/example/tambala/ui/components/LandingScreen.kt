package com.example.tambala.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tambala.ui.components.PrimaryButton
import com.example.tambala.ui.theme.GreenPrimary
//import com.example.tambala.ui.theme.TextPrimary
import com.example.tambala.ui.theme.TextSecondary

@Composable
fun LandingScreen(
    onGetStartedClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Icon / Logo
        Icon(
            imageVector = Icons.Default.School,
            contentDescription = "Tambala Logo",
            tint = Color.White,
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(GreenPrimary)
                .padding(20.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // App Name
        Text(
            text = "Tambala",
            color = GreenPrimary,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Tagline
        Text(
            text = "Track. Budget. Succeed.",
            color = TextSecondary,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
       /* Text(
            text = "Manage your semester upkeep easily.\nKnow where your money goes and make it last.",
            color = TextSecondary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )*/

        Spacer(modifier = Modifier.height(48.dp))

        // Get Started Button
        PrimaryButton(
            text = "Get Started",
            onClick = onGetStartedClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LandingScreenPreview() {
    LandingScreen()
}