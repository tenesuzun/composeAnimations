package com.tenesuzun.composeanimations.ui.effects

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.tenesuzun.composeanimations.ui.neumorphCached

@Composable
fun DemoNeumorphism() {
    var pressed by remember { mutableStateOf(false) }

    val depth by animateDpAsState(
        targetValue = if (pressed) 2.dp else 10.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = 0.8f),
        label = "neoDepth"
    )

    val base = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
            .neumorphCached(baseColor = base, cornerRadius = 18.dp, depth = depth, pressed = pressed)
            .clip(RoundedCornerShape(18.dp))
            .clickable { pressed = !pressed }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = if (pressed) "Neumorphism (pressed)" else "Neumorphism (raised)",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}