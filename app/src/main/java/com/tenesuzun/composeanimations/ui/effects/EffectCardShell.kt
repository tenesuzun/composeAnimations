package com.tenesuzun.composeanimations.ui.effects

import android.os.Build
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.tenesuzun.composeanimations.EffectMode
import com.tenesuzun.composeanimations.ui.blurLayer
import com.tenesuzun.composeanimations.ui.glow
import com.tenesuzun.composeanimations.ui.neumorphCached
import com.tenesuzun.composeanimations.ui.theme.effectAccent

/**
 * Wrapper (SARMALAYAN :D):
 * - Efekt seçimi için state tutar
 * - seçili efekti her bir kart için uygular
 * - Gözle ayırt edilebilir farklılıklar uygular (Kısmen)
 */
@Composable
fun EffectCardShell(
    effectMode: EffectMode,
    scrollProgress: Float,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    val accent = effectAccent(effectMode)

    //
    val glowAlpha by animateFloatAsState(
        targetValue = if (effectMode == EffectMode.HoverGlow) (if (pressed) 0.85f else 0.18f) else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "cardGlowAlpha"
    )
    val glowRadius by animateDpAsState(
        targetValue = if (effectMode == EffectMode.HoverGlow) (if (pressed) 22.dp else 10.dp) else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "cardGlowRadius"
    )

    val shadowElev by animateDpAsState(
        targetValue = when (effectMode) {
            EffectMode.DynamicShadow -> if (pressed) 18.dp else 6.dp
            EffectMode.Glassmorphism -> 14.dp
            EffectMode.BlurOnScroll -> 10.dp
            else -> 0.dp
        },
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = 0.78f),
        label = "cardShadow"
    )

    val scale by animateFloatAsState(
        targetValue = if (effectMode == EffectMode.DynamicShadow) (if (pressed) 0.985f else 1f) else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = 0.78f),
        label = "cardScale"
    )

    // blur grows with scroll (only used in BlurOnScroll mode)
    val blurDp by animateDpAsState(
        targetValue = if (effectMode == EffectMode.BlurOnScroll) (2.dp + 18.dp * scrollProgress) else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "cardBlur"
    )

    // Neumorph “depth” animates with press
    val neoDepth by animateDpAsState(
        targetValue = if (effectMode == EffectMode.Neumorphism) (if (pressed) 2.dp else 10.dp) else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = 0.82f),
        label = "neoDepth"
    )

    // Stronger base colors per mode so your eyes can spot differences quickly
    val baseColor = when (effectMode) {
        EffectMode.Glassmorphism -> MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
        EffectMode.BlurOnScroll -> MaterialTheme.colorScheme.surface.copy(alpha = 0.62f)
        EffectMode.Neumorphism -> MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.80f)
    }

    val borderColor = when (effectMode) {
        EffectMode.Glassmorphism -> Color.White.copy(alpha = 0.16f)
        EffectMode.BlurOnScroll -> accent.copy(alpha = 0.35f)
        EffectMode.HoverGlow -> accent.copy(alpha = 0.35f)
        EffectMode.DynamicShadow -> accent.copy(alpha = 0.22f)
        EffectMode.Neumorphism -> Color.Transparent
    }

    // Build the outer modifier (effects live OUTSIDE clip)
    var m = modifier

    if (effectMode == EffectMode.HoverGlow) {
        m = m.glow(
            color = accent,
            alpha = glowAlpha,
            blurRadius = glowRadius,
            cornerRadius = 8.dp,
//            cornerRadius = shape.topStart,
            spread = 4.dp
        )
    }

    if (shadowElev > 0.dp) {
        m = m.shadow(shadowElev, shape)
    }

    if (effectMode == EffectMode.DynamicShadow) {
        m = m.graphicsLayer {
            scaleX = scale
            scaleY = scale
            translationY = if (pressed) -1.5f else 0f
        }
    }

    if (effectMode == EffectMode.Neumorphism && neoDepth > 0.dp) {
        m = m.neumorphCached(
            baseColor = baseColor,
            cornerRadius = 8.dp,
//            cornerRadius = shape.topStart,
            depth = neoDepth,
            pressed = pressed
        )
    }

    Box(
        modifier = m
            .clip(shape)
            .then(
                if (effectMode != EffectMode.Neumorphism)
                    Modifier.background(baseColor)
                else Modifier // neumorph draws its own base
            )
            .then(if (borderColor != Color.Transparent) Modifier.border(1.dp, borderColor, shape) else Modifier)
            .clickable(interactionSource = interaction, indication = null) { /* just for study */ }
    ) {
        // Accent stripe (very visible)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            accent.copy(alpha = 0.85f),
                            accent.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Underlay that gets blurred (keeps TEXT crisp; only the underlay blurs)
        if (effectMode == EffectMode.BlurOnScroll || effectMode == EffectMode.Glassmorphism) {
            val underlayBlur = when (effectMode) {
                EffectMode.BlurOnScroll -> blurDp
                EffectMode.Glassmorphism -> 10.dp
                else -> 0.dp
            }

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                accent.copy(alpha = 0.20f),
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.14f),
                                Color.Transparent
                            )
                        )
                    )
                    .then(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Modifier.blurLayer(underlayBlur) else Modifier)
            )
        }

        // Content on top (not blurred)
        content()
    }
}