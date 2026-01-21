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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tenesuzun.composeanimations.R

@Composable
fun GlassBottomBar(modifier: Modifier = Modifier) {
    Surface(
        color = Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp)
//            .shadow(18.dp, RoundedCornerShape(22.dp))
//            .clip(RoundedCornerShape(22.dp))
//            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(22.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.65f))
                .padding(horizontal = 18.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) { Icon(painterResource(R.drawable.ic_home), contentDescription = "Home") }
            IconButton(onClick = {}) { Icon(painterResource(R.drawable.ic_search), contentDescription = "Search") }
            IconButton(onClick = {}) { Icon(painterResource(R.drawable.ic_favorite), contentDescription = "Favorites") }
            IconButton(onClick = {}) { Icon(painterResource(R.drawable.ic_person), contentDescription = "Profile") }
        }
    }
}