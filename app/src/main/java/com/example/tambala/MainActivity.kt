package com.example.tambala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.tambala.ui.pages.AnalysisScreen
import com.example.tambala.ui.pages.BudgetsScreen
import com.example.tambala.ui.pages.CategoriesScreen
import com.example.tambala.ui.pages.DashboardScreen
import com.example.tambala.ui.pages.LandingScreen
import com.example.tambala.ui.pages.SplashPage
import com.example.tambala.ui.theme.TambalaTheme

enum class AppScreen {
    Splash,
    Landing,
    Dashboard,
    Budgets,
    Categories,
    Analysis
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TambalaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainApp()
                }
            }
        }
    }
}

@Composable
fun MainApp() {
    var currentScreen by remember { mutableStateOf(AppScreen.Splash) }

    val onNavigateToTab: (Int) -> Unit = { tabIndex ->
        currentScreen = when (tabIndex) {
            0 -> AppScreen.Dashboard
            1 -> AppScreen.Budgets
            2 -> AppScreen.Categories
            3 -> AppScreen.Analysis
            else -> AppScreen.Dashboard
        }
    }

    when (currentScreen) {
        AppScreen.Splash -> {
            SplashPage(
                onTimeout = { currentScreen = AppScreen.Landing }
            )
        }
        AppScreen.Landing -> {
            LandingScreen(
                onGetStartedClick = { currentScreen = AppScreen.Dashboard }
            )
        }
        AppScreen.Dashboard -> {
            DashboardScreen(
                onNavigateToTab = onNavigateToTab,
                onNavigateToBudgets = { currentScreen = AppScreen.Budgets },
                onNavigateToCategories = { currentScreen = AppScreen.Categories },
                onNavigateToAnalysis = { currentScreen = AppScreen.Analysis }
            )
        }
        AppScreen.Budgets -> {
            BudgetsScreen(
                onNavigateToTab = onNavigateToTab
            )
        }
        AppScreen.Categories -> {
            CategoriesScreen(
                onNavigateToTab = onNavigateToTab
            )
        }
        AppScreen.Analysis -> {
            AnalysisScreen(
                onNavigateToTab = onNavigateToTab
            )
        }
    }
}
