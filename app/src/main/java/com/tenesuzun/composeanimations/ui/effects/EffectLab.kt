package com.tenesuzun.composeanimations.ui.effects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.tenesuzun.composeanimations.EffectMode
import com.tenesuzun.composeanimations.ui.theme.effectAccent

/**
 * Efekt seçimlerinin yapıldığı widget
 */
@Composable
fun EffectsLab(effectMode: EffectMode, onSelect: (EffectMode) -> Unit, listState: LazyListState) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Effects Lab", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(EffectMode.entries) { mode ->
                EffectChip(
                    text = mode.name,
                    selected = (mode == effectMode),
                    accent = effectAccent(mode),
                    onClick = { onSelect(mode) }
                )
            }
        }

        when (effectMode) {
            EffectMode.HoverGlow -> DemoHoverGlow()
            EffectMode.DynamicShadow -> DemoDynamicShadow()
            EffectMode.BlurOnScroll -> DemoBlurOnScroll(listState)
            EffectMode.Glassmorphism -> DemoGlassmorphism()
            EffectMode.Neumorphism -> DemoNeumorphism()
        }
    }
}