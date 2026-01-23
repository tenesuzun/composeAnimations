package com.tenesuzun.composeanimations.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object Menu : Screen("menu")
}