package com.tenesuzun.composeanimations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tenesuzun.composeanimations.navigation.Screen
import com.tenesuzun.composeanimations.ui.screens.FavoritesScreen
import com.tenesuzun.composeanimations.ui.screens.HomeScreen
import com.tenesuzun.composeanimations.ui.screens.MenuScreen
import com.tenesuzun.composeanimations.ui.screens.ProfileScreen
import com.tenesuzun.composeanimations.ui.screens.SearchScreen
import com.tenesuzun.composeanimations.ui.systembars.GlassBottomBar
import com.tenesuzun.composeanimations.ui.systembars.GlassTopBar
import com.tenesuzun.composeanimations.ui.theme.ModernTheme
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState

@OptIn(ExperimentalHazeMaterialsApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ModernTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val hazeState = rememberHazeState()
                val listState = rememberLazyListState()

                Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier
                            .fillMaxSize()
                            .safeContentPadding()
                            .systemBarsPadding()
                            .hazeSource(state = hazeState)
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(listState = listState)
                        }
                        composable(Screen.Search.route) {
                            SearchScreen()
                        }
                        composable(Screen.Favorites.route) {
                            FavoritesScreen()
                        }
                        composable(Screen.Profile.route) {
                            ProfileScreen()
                        }
                        composable(Screen.Menu.route) {
                            MenuScreen()
                        }
                    }

                    // Top Bar
                    GlassTopBar(
                        title = "Topbar",
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .hazeEffect(state = hazeState, style = HazeMaterials.ultraThin())
                            .windowInsetsPadding(WindowInsets.statusBars)
                    )

                    // Bottom Bar
                    GlassBottomBar(
                        currentRoute = currentRoute,
                        onNavigate = { screen ->
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .hazeEffect(state = hazeState, style = HazeMaterials.ultraThin())
                    )
                }
            }
        }
    }
}