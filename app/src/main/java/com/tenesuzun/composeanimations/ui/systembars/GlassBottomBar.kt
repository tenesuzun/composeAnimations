package com.tenesuzun.composeanimations.ui.systembars

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tenesuzun.composeanimations.R

@Composable
fun GlassBottomBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 12.dp)
//            .shadow(18.dp, RoundedCornerShape(28.dp))
//            .clip(RoundedCornerShape(28.dp))
//            .border(
//                width = Dp.Hairline,
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        Color.White.copy(alpha = .8f),
//                        Color.White.copy(alpha = .2f),
//                    ),
//                ),
//                shape = RoundedCornerShape(28.dp)
//            )
//            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
            .padding(horizontal = 18.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(
                painterResource(R.drawable.ic_home),
                contentDescription = "Home",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(onClick = {}) {
            Icon(
                painterResource(R.drawable.ic_search),
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(onClick = {}) {
            Icon(
                painterResource(R.drawable.ic_favorite),
                contentDescription = "Favorites",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(onClick = {}) {
            Icon(
                painterResource(R.drawable.ic_person),
                contentDescription = "Profile",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}