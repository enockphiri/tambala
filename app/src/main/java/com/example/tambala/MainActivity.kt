package com.example.tambala

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import com.example.tambala.ui.pages.AnalysisScreen
import com.example.tambala.ui.pages.BudgetsScreen
import com.example.tambala.ui.pages.CategoriesScreen
import com.example.tambala.ui.pages.DashboardScreen
import com.example.tambala.ui.pages.LandingScreen
import com.example.tambala.ui.pages.SettingsScreen
import com.example.tambala.ui.pages.SplashPage
import com.example.tambala.ui.pages.TransactionsScreen
import com.example.tambala.ui.theme.TambalaTheme
import com.example.tambala.ui.viewmodel.FinanceViewModel

enum class AppScreen {
    Splash,
    Landing,
    Dashboard,
    Budgets,
    Categories,
    Analysis,
    Transactions,
    Settings
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences("tambala_prefs", MODE_PRIVATE)
        val hasSeenOnboarding = prefs.getBoolean("has_seen_onboarding", false)

        setContent {
            TambalaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainApp(initialHasSeenOnboarding = hasSeenOnboarding)
                }
            }
        }
    }
}

@Composable
fun MainApp(initialHasSeenOnboarding: Boolean) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("tambala_prefs", Context.MODE_PRIVATE) }
    val viewModel = remember { FinanceViewModel(context.applicationContext) }

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
                onTimeout = {
                    currentScreen = if (initialHasSeenOnboarding) AppScreen.Dashboard else AppScreen.Landing
                }
            )
        }
        AppScreen.Landing -> {
            LandingScreen(
                onGetStartedClick = {
                    prefs.edit().putBoolean("has_seen_onboarding", true).apply()
                    currentScreen = AppScreen.Dashboard
                }
            )
        }
        AppScreen.Dashboard -> {
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToTab = onNavigateToTab,
                onNavigateToBudgets = { currentScreen = AppScreen.Budgets },
                onNavigateToCategories = { currentScreen = AppScreen.Categories },
                onNavigateToAnalysis = { currentScreen = AppScreen.Analysis },
                onNavigateToTransactions = { currentScreen = AppScreen.Transactions },
                onSettingsClick = { currentScreen = AppScreen.Settings }
            )
        }
        AppScreen.Budgets -> {
            BudgetsScreen(
                viewModel = viewModel,
                onNavigateToTab = onNavigateToTab,
                onSettingsClick = { currentScreen = AppScreen.Settings }
            )
        }
        AppScreen.Categories -> {
            CategoriesScreen(
                viewModel = viewModel,
                onNavigateToTab = onNavigateToTab,
                onSettingsClick = { currentScreen = AppScreen.Settings }
            )
        }
        AppScreen.Analysis -> {
            AnalysisScreen(
                viewModel = viewModel,
                onNavigateToTab = onNavigateToTab
            )
        }
        AppScreen.Transactions -> {
            TransactionsScreen(
                viewModel = viewModel,
                onNavigateToTab = onNavigateToTab
            )
        }
        AppScreen.Settings -> {
            SettingsScreen(
                viewModel = viewModel,
                onNavigateToTab = onNavigateToTab
            )
        }
    }
}
