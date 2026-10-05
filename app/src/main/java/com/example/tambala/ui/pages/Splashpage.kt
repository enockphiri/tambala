package com.example.tambala.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tambala.ui.theme.GreenPrimary
import kotlinx.coroutines.delay

@Composable
fun SplashPage(
    onTimeout: () -> Unit = {}
) {
    // Automatically move to next screen after 2 seconds
    LaunchedEffect(key1 = Unit) {
        delay(timeMillis = 2000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GreenPrimary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Icon
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = "Tambala Logo",
                tint = GreenPrimary,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(18.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // App Name
            Text(
                text = "Tambala",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Track. Budget. Succeed.",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 15.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashPagePreview() {
    SplashPage(
        onTimeout = {}
    )
}