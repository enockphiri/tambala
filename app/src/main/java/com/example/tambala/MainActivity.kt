package com.example.tambala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
//import com.example.tambala.ui.components.BalanceCard
import com.example.tambala.ui.theme.TambalaTheme
//import com.example.tambala.ui.components.BalanceCard
//import com.example.tambala.ui.pages.BudgetsScreen
import com.example.tambala.ui.pages.DashboardScreen
//import com.example.tambala.ui.pages.WalletsScreen

//import com.example.tambala.ui.pages.SplashPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TambalaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    DashboardScreen()




                }
            }
        }
    }
}