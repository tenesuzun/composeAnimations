package com.tenesuzun.composeanimations.ui.systembars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tenesuzun.composeanimations.R
import com.tenesuzun.composeanimations.navigation.Screen

@Composable
fun GlassBottomBar(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onNavigate(Screen.Home) }) {
            Icon(
                painterResource(R.drawable.ic_home),
                contentDescription = "Home",
                tint = if (currentRoute == Screen.Home.route)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(onClick = { onNavigate(Screen.Search) }) {
            Icon(
                painterResource(R.drawable.ic_search),
                contentDescription = "Search",
                tint = if (currentRoute == Screen.Search.route)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(onClick = { onNavigate(Screen.Favorites) }) {
            Icon(
                painterResource(R.drawable.ic_favorite),
                contentDescription = "Favorites",
                tint = if (currentRoute == Screen.Favorites.route)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(onClick = { onNavigate(Screen.Profile) }) {
            Icon(
                painterResource(R.drawable.ic_person),
                contentDescription = "Profile",
                tint = if (currentRoute == Screen.Profile.route)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(onClick = { onNavigate(Screen.Menu) }) {
            Icon(
                painterResource(R.drawable.ic_menu),
                contentDescription = "Menu",
                tint = if (currentRoute == Screen.Menu.route)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}