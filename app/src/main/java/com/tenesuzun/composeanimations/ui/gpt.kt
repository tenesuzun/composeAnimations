package com.tenesuzun.composeanimations.ui
//
//import com.tenesuzun.composeanimations.R
//
//package com.tenesuzun.composeanimations
//
//import android.graphics.BlurMaskFilter
//import android.graphics.RectF
//import android.os.Build
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.animation.core.Spring
//import androidx.compose.animation.core.animateDpAsState
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.spring
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.interaction.collectIsPressedAsState
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.WindowInsets
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.navigationBars
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.statusBars
//import androidx.compose.foundation.layout.windowInsetsPadding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyListState
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.darkColorScheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.derivedStateOf
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.composed
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.drawWithCache
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.geometry.CornerRadius
//import androidx.compose.ui.graphics.BlurEffect
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Paint
//import androidx.compose.ui.graphics.TileMode
//import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.graphics.nativeCanvas
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import kotlin.math.min
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            ModernTheme {
//                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    MockSceneScreen()
//                }
//            }
//        }
//    }
//}
//
//private enum class EffectMode {
//    HoverGlow, DynamicShadow, BlurOnScroll, Glassmorphism, Neumorphism
//}
//
//@Composable
//fun MockSceneScreen() {
//    val listState = rememberLazyListState()
//
//    val verticalItems = remember {
//        (1..5).map { i -> "Vertical card item #$i — two lines max example title goes here." }
//    }
//    val horizontalItems = remember {
//        (1..12).map { i -> "Horizontal item #$i" }
//    }
//
//    var effectMode by remember { mutableStateOf(EffectMode.HoverGlow) }
//
//    // 0..1 progress as you scroll a little
//    val scrollProgress by remember(listState) {
//        derivedStateOf {
//            val px = listState.firstVisibleItemScrollOffset.toFloat()
//            min(1f, px / 240f)
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(appBackgroundBrush())
//            .windowInsetsPadding(WindowInsets.statusBars)
//    ) {
//        Column(modifier = Modifier.fillMaxSize()) {
//
//            GlassTopBar(
//                title = "Topbar",
//                progress = scrollProgress,
//            )
//
//            Box(modifier = Modifier.weight(1f)) {
//                LazyColumn(
//                    state = listState,
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.spacedBy(12.dp),
//                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp)
//                ) {
//                    item {
//                        EffectsLab(
//                            effectMode = effectMode,
//                            onSelect = { effectMode = it },
//                            listState = listState
//                        )
//                    }
//
//                    // Top 5 vertical cards
//                    items(verticalItems) { title ->
//                        VerticalCard(
//                            title = title,
//                            effectMode = effectMode,
//                            scrollProgress = scrollProgress
//                        )
//                    }
//
//                    // Horizontal row section
//                    item {
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Text(
//                            text = "Horizontal section",
//                            style = MaterialTheme.typography.titleMedium,
//                            color = MaterialTheme.colorScheme.onBackground,
//                            modifier = Modifier.padding(bottom = 8.dp)
//                        )
//
//                        LazyRow(
//                            horizontalArrangement = Arrangement.spacedBy(12.dp),
//                            contentPadding = PaddingValues(end = 8.dp)
//                        ) {
//                            items(horizontalItems) { label ->
//                                HorizontalCard(
//                                    label = label,
//                                    effectMode = effectMode,
//                                    scrollProgress = scrollProgress
//                                )
//                            }
//                        }
//                        Spacer(modifier = Modifier.height(8.dp))
//                    }
//
//                    // Repeat bottom 5 vertical cards
//                    items(verticalItems) { title ->
//                        VerticalCard(
//                            title = title,
//                            effectMode = effectMode,
//                            scrollProgress = scrollProgress
//                        )
//                    }
//                }
//            }
//
//            GlassBottomBar(
//                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
//            )
//        }
//    }
//}
//
///* -------------------- Top / Bottom Bars -------------------- */
//
//@Composable
//private fun GlassTopBar(title: String, progress: Float) {
//    val alpha by animateFloatAsState(
//        targetValue = 0.55f + 0.25f * progress,
//        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
//        label = "topbarAlpha"
//    )
//
//    val blurDp by animateDpAsState(
//        targetValue = (6.dp + 14.dp * progress),
//        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
//        label = "topbarBlur"
//    )
//
//    Surface(
//        color = Color.Transparent,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 14.dp, vertical = 10.dp)
//            .shadow(12.dp, RoundedCornerShape(18.dp))
//            .clip(RoundedCornerShape(18.dp))
//            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(18.dp))
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(MaterialTheme.colorScheme.surface.copy(alpha = alpha))
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp)
//                    .background(
//                        Brush.linearGradient(
//                            listOf(
//                                MaterialTheme.colorScheme.primary.copy(alpha = 0.22f),
//                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.16f),
//                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.20f),
//                            )
//                        )
//                    )
//                    .then(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Modifier.blurLayer(blurDp) else Modifier)
//            )
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp, vertical = 14.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(text = title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
//                Spacer(modifier = Modifier.weight(1f))
//                Text(
//                    text = "scroll: ${(progress * 100).toInt()}%",
//                    style = MaterialTheme.typography.labelMedium,
//                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun GlassBottomBar(modifier: Modifier = Modifier) {
//    Surface(
//        color = Color.Transparent,
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(horizontal = 14.dp, vertical = 12.dp)
//            .shadow(18.dp, RoundedCornerShape(22.dp))
//            .clip(RoundedCornerShape(22.dp))
//            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(22.dp))
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.65f))
//                .padding(horizontal = 18.dp, vertical = 10.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(onClick = {}) { Icon(painterResource(R.drawable.ic_home), contentDescription = "Home") }
//            IconButton(onClick = {}) { Icon(painterResource(R.drawable.ic_search), contentDescription = "Search") }
//            IconButton(onClick = {}) { Icon(painterResource(R.drawable.ic_favorite), contentDescription = "Favorites") }
//            IconButton(onClick = {}) { Icon(painterResource(R.drawable.ic_person), contentDescription = "Profile") }
//        }
//    }
//}
//
///* -------------------- Cards (NOW affected by EffectMode) -------------------- */
//
//@Composable
//private fun VerticalCard(title: String, effectMode: EffectMode, scrollProgress: Float) {
//    val shape = RoundedCornerShape(16.dp)
//
//    EffectCardShell(
//        effectMode = effectMode,
//        scrollProgress = scrollProgress,
//        shape = shape,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(14.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(56.dp)
//                    .clip(RoundedCornerShape(14.dp))
//                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.92f)),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.ic_photo),
//                    contentDescription = "Placeholder image",
//                    modifier = Modifier.size(26.dp),
//                    tint = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//
//            Spacer(modifier = Modifier.size(12.dp))
//
//            Text(
//                text = title,
//                style = MaterialTheme.typography.bodyLarge,
//                color = MaterialTheme.colorScheme.onSurface,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//    }
//}
//
//@Composable
//private fun HorizontalCard(label: String, effectMode: EffectMode, scrollProgress: Float) {
//    val shape = RoundedCornerShape(18.dp)
//
//    EffectCardShell(
//        effectMode = effectMode,
//        scrollProgress = scrollProgress,
//        shape = shape,
//        modifier = Modifier.size(width = 160.dp, height = 190.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(14.dp),
//            verticalArrangement = Arrangement.spacedBy(10.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(110.dp)
//                    .clip(RoundedCornerShape(16.dp))
//                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.92f)),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.ic_photo),
//                    contentDescription = "Placeholder image",
//                    modifier = Modifier.size(30.dp),
//                    tint = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//
//            Text(
//                text = label,
//                style = MaterialTheme.typography.bodyLarge,
//                color = MaterialTheme.colorScheme.onSurface,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis
//            )
//        }
//    }
//}
//
///**
// * Single wrapper that:
// * - provides press interaction state
// * - applies the currently selected effect to any card
// * - uses strong accent cues (stripe + border tint) so you can "see it with your eyes"
// */
//@Composable
//private fun EffectCardShell(
//    effectMode: EffectMode,
//    scrollProgress: Float,
//    shape: RoundedCornerShape,
//    modifier: Modifier = Modifier,
//    content: @Composable () -> Unit
//) {
//    val interaction = remember { MutableInteractionSource() }
//    val pressed by interaction.collectIsPressedAsState()
//
//    val accent = effectAccent(effectMode)
//
//    // Animated knobs (only “active” modes will visibly use them)
//    val glowAlpha by animateFloatAsState(
//        targetValue = if (effectMode == EffectMode.HoverGlow) (if (pressed) 0.85f else 0.18f) else 0f,
//        animationSpec = spring(stiffness = Spring.StiffnessLow),
//        label = "cardGlowAlpha"
//    )
//    val glowRadius by animateDpAsState(
//        targetValue = if (effectMode == EffectMode.HoverGlow) (if (pressed) 22.dp else 10.dp) else 0.dp,
//        animationSpec = spring(stiffness = Spring.StiffnessLow),
//        label = "cardGlowRadius"
//    )
//
//    val shadowElev by animateDpAsState(
//        targetValue = when (effectMode) {
//            EffectMode.DynamicShadow -> if (pressed) 18.dp else 6.dp
//            EffectMode.Glassmorphism -> 14.dp
//            EffectMode.BlurOnScroll -> 10.dp
//            else -> 0.dp
//        },
//        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = 0.78f),
//        label = "cardShadow"
//    )
//
//    val scale by animateFloatAsState(
//        targetValue = if (effectMode == EffectMode.DynamicShadow) (if (pressed) 0.985f else 1f) else 1f,
//        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = 0.78f),
//        label = "cardScale"
//    )
//
//    // blur grows with scroll (only used in BlurOnScroll mode)
//    val blurDp by animateDpAsState(
//        targetValue = if (effectMode == EffectMode.BlurOnScroll) (2.dp + 18.dp * scrollProgress) else 0.dp,
//        animationSpec = spring(stiffness = Spring.StiffnessLow),
//        label = "cardBlur"
//    )
//
//    // Neumorph “depth” animates with press
//    val neoDepth by animateDpAsState(
//        targetValue = if (effectMode == EffectMode.Neumorphism) (if (pressed) 2.dp else 10.dp) else 0.dp,
//        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = 0.82f),
//        label = "neoDepth"
//    )
//
//    // Stronger base colors per mode so your eyes can spot differences quickly
//    val baseColor = when (effectMode) {
//        EffectMode.Glassmorphism -> MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
//        EffectMode.BlurOnScroll -> MaterialTheme.colorScheme.surface.copy(alpha = 0.62f)
//        EffectMode.Neumorphism -> MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
//        else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.80f)
//    }
//
//    val borderColor = when (effectMode) {
//        EffectMode.Glassmorphism -> Color.White.copy(alpha = 0.16f)
//        EffectMode.BlurOnScroll -> accent.copy(alpha = 0.35f)
//        EffectMode.HoverGlow -> accent.copy(alpha = 0.35f)
//        EffectMode.DynamicShadow -> accent.copy(alpha = 0.22f)
//        EffectMode.Neumorphism -> Color.Transparent
//    }
//
//    // Build the outer modifier (effects live OUTSIDE clip)
//    var m = modifier
//
//    if (effectMode == EffectMode.HoverGlow) {
//        m = m.glow(
//            color = accent,
//            alpha = glowAlpha,
//            blurRadius = glowRadius,
//            cornerRadius = 8.dp,
////            cornerRadius = shape.topStart,
//            spread = 4.dp
//        )
//    }
//
//    if (shadowElev > 0.dp) {
//        m = m.shadow(shadowElev, shape)
//    }
//
//    if (effectMode == EffectMode.DynamicShadow) {
//        m = m.graphicsLayer {
//            scaleX = scale
//            scaleY = scale
//            translationY = if (pressed) -1.5f else 0f
//        }
//    }
//
//    if (effectMode == EffectMode.Neumorphism) {
//        m = m.neumorphCached(
//            baseColor = baseColor,
//            cornerRadius = 8.dp,
////            cornerRadius = shape.topStart,
//            depth = neoDepth,
//            pressed = pressed
//        )
//    }
//
//    Box(
//        modifier = m
//            .clip(shape)
//            .then(
//                if (effectMode != EffectMode.Neumorphism)
//                    Modifier.background(baseColor)
//                else Modifier // neumorph draws its own base
//            )
//            .then(if (borderColor != Color.Transparent) Modifier.border(1.dp, borderColor, shape) else Modifier)
//            .clickable(interactionSource = interaction, indication = null) { /* just for study */ }
//    ) {
//        // Accent stripe (very visible)
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(3.dp)
//                .background(
//                    Brush.horizontalGradient(
//                        listOf(
//                            accent.copy(alpha = 0.85f),
//                            accent.copy(alpha = 0.25f),
//                            Color.Transparent
//                        )
//                    )
//                )
//        )
//
//        // Underlay that gets blurred (keeps TEXT crisp; only the underlay blurs)
//        if (effectMode == EffectMode.BlurOnScroll || effectMode == EffectMode.Glassmorphism) {
//            val underlayBlur = when (effectMode) {
//                EffectMode.BlurOnScroll -> blurDp
//                EffectMode.Glassmorphism -> 10.dp
//                else -> 0.dp
//            }
//
//            Box(
//                modifier = Modifier
//                    .matchParentSize()
//                    .background(
//                        Brush.linearGradient(
//                            listOf(
//                                accent.copy(alpha = 0.20f),
//                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.14f),
//                                Color.Transparent
//                            )
//                        )
//                    )
//                    .then(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Modifier.blurLayer(underlayBlur) else Modifier)
//            )
//        }
//
//        // Content on top (not blurred)
//        content()
//    }
//}
//
///* -------------------- Effects Lab (demo boxes stay) -------------------- */
//
//@Composable
//private fun EffectsLab(effectMode: EffectMode, onSelect: (EffectMode) -> Unit, listState: LazyListState) {
//    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//        Text(text = "Effects Lab", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
//
//        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
//            items(EffectMode.entries) { mode ->
//                EffectChip(
//                    text = mode.name,
//                    selected = (mode == effectMode),
//                    accent = effectAccent(mode),
//                    onClick = { onSelect(mode) }
//                )
//            }
//        }
//
//        when (effectMode) {
//            EffectMode.HoverGlow -> DemoHoverGlow()
//            EffectMode.DynamicShadow -> DemoDynamicShadow()
//            EffectMode.BlurOnScroll -> DemoBlurOnScroll(listState)
//            EffectMode.Glassmorphism -> DemoGlassmorphism()
//            EffectMode.Neumorphism -> DemoNeumorphism()
//        }
//    }
//}
//
//@Composable
//private fun EffectChip(text: String, selected: Boolean, accent: Color, onClick: () -> Unit) {
//    val bg = if (selected) accent.copy(alpha = 0.22f)
//    else MaterialTheme.colorScheme.surface.copy(alpha = 0.70f)
//
//    val border = if (selected) accent.copy(alpha = 0.65f)
//    else Color.White.copy(alpha = 0.10f)
//
//    Box(
//        modifier = Modifier
//            .clip(CircleShape)
//            .background(bg)
//            .border(1.dp, border, CircleShape)
//            .clickable(onClick = onClick)
//            .padding(horizontal = 14.dp, vertical = 10.dp)
//    ) {
//        Text(text = text, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface)
//    }
//}
//
///* -------------------- Demos (unchanged, still useful) -------------------- */
//
//@Composable
//private fun DemoHoverGlow() {
//    val interaction = remember { MutableInteractionSource() }
//    val pressed by interaction.collectIsPressedAsState()
//
//    val glowAlpha by animateFloatAsState(
//        targetValue = if (pressed) 0.85f else 0.25f,
//        animationSpec = spring(stiffness = Spring.StiffnessLow),
//        label = "glowAlpha"
//    )
//    val glowRadius by animateDpAsState(
//        targetValue = if (pressed) 22.dp else 10.dp,
//        animationSpec = spring(stiffness = Spring.StiffnessLow),
//        label = "glowRadius"
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .glow(
//                color = MaterialTheme.colorScheme.secondary,
//                alpha = glowAlpha,
//                blurRadius = glowRadius,
//                cornerRadius = 18.dp,
//                spread = 4.dp
//            )
//            .clip(RoundedCornerShape(18.dp))
//            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.75f))
//            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(18.dp))
//            .clickable(interactionSource = interaction, indication = null) { }
//            .padding(16.dp)
//    ) {
//        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
//            Text("Press me for glow", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
//            Text(
//                "Now the SAME glow style also applies to list cards when HoverGlow is selected.",
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//            )
//        }
//    }
//}
//
//@Composable
//private fun DemoDynamicShadow() {
//    val interaction = remember { MutableInteractionSource() }
//    val pressed by interaction.collectIsPressedAsState()
//
//    val elevation by animateDpAsState(
//        targetValue = if (pressed) 18.dp else 6.dp,
//        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = 0.75f),
//        label = "dynElev"
//    )
//    val scale by animateFloatAsState(
//        targetValue = if (pressed) 0.985f else 1f,
//        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = 0.75f),
//        label = "dynScale"
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .graphicsLayer {
//                scaleX = scale
//                scaleY = scale
//                translationY = if (pressed) -1.5f else 0f
//            }
//            .shadow(elevation, RoundedCornerShape(18.dp))
//            .clip(RoundedCornerShape(18.dp))
//            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.78f))
//            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(18.dp))
//            .clickable(interactionSource = interaction, indication = null) { }
//            .padding(16.dp)
//    ) {
//        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
//            Text("Dynamic shadow (press)", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
//            Text(
//                "Selected mode now applies to every list card too (same press lift).",
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//            )
//        }
//    }
//}
//
//@Composable
//private fun DemoBlurOnScroll(listState: LazyListState) {
//    val progress by remember(listState) {
//        derivedStateOf { min(1f, listState.firstVisibleItemScrollOffset / 260f) }
//    }
//    val blurDp by animateDpAsState(
//        targetValue = (2.dp + 18.dp * progress),
//        animationSpec = spring(stiffness = Spring.StiffnessLow),
//        label = "scrollBlur"
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(18.dp))
//            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(18.dp))
//            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.70f))
//    ) {
//        Box(
//            modifier = Modifier
//                .matchParentSize()
//                .background(
//                    Brush.linearGradient(
//                        listOf(
//                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.22f),
//                            MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
//                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f),
//                        )
//                    )
//                )
//                .then(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Modifier.blurLayer(blurDp) else Modifier)
//        )
//
//        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
//            Text("Blur-on-scroll", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
//            Text(
//                "Select BlurOnScroll → every list card gets a blurred underlay driven by scrollProgress (text stays sharp).",
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//            )
//        }
//    }
//}
//
//@Composable
//private fun DemoGlassmorphism() {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .shadow(14.dp, RoundedCornerShape(20.dp))
//            .clip(RoundedCornerShape(20.dp))
//            .border(1.dp, Color.White.copy(alpha = 0.16f), RoundedCornerShape(20.dp))
//            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.45f))
//            .padding(16.dp)
//    ) {
//        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
//            Text("Glassmorphism", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
//            Text(
//                "Select Glassmorphism → list cards become more translucent with a blurred accent underlay.",
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//            )
//        }
//    }
//}
//
//@Composable
//private fun DemoNeumorphism() {
//    var pressed by remember { mutableStateOf(false) }
//
//    val depth by animateDpAsState(
//        targetValue = if (pressed) 2.dp else 10.dp,
//        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = 0.8f),
//        label = "neoDepth"
//    )
//
//    val base = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(74.dp)
//            .neumorphCached(baseColor = base, cornerRadius = 18.dp, depth = depth, pressed = pressed)
//            .clip(RoundedCornerShape(18.dp))
//            .clickable { pressed = !pressed }
//            .padding(horizontal = 16.dp),
//        contentAlignment = Alignment.CenterStart
//    ) {
//        Text(
//            text = if (pressed) "Neumorphism (pressed)" else "Neumorphism (raised)",
//            style = MaterialTheme.typography.titleMedium,
//            color = MaterialTheme.colorScheme.onSurface
//        )
//    }
//}
//
///* -------------------- Colors / Theme -------------------- */
//
//@Composable
//private fun ModernTheme(content: @Composable () -> Unit) {
//    val scheme = darkColorScheme(
//        // Keep base modern/dark
//        primary = Color(0xFF8B5CF6),   // violet
//        secondary = Color(0xFF22D3EE), // cyan
//        tertiary = Color(0xFFFFB86B),  // warm accent
//        background = Color(0xFF070A12),
//        surface = Color(0xFF0E1424),
//        surfaceVariant = Color(0xFF151F36),
//        onBackground = Color(0xFFE7EAF0),
//        onSurface = Color(0xFFE7EAF0),
//        onSurfaceVariant = Color(0xFFB8C2D6),
//    )
//    MaterialTheme(colorScheme = scheme, content = content)
//}
//
//private fun appBackgroundBrush(): Brush =
//    Brush.linearGradient(
//        colors = listOf(
//            Color(0xFF060913),
//            Color(0xFF09112A),
//            Color(0xFF070A12),
//        )
//    )
//
//private fun effectAccent(mode: EffectMode): Color = when (mode) {
//    EffectMode.HoverGlow -> Color(0xFF22D3EE)      // cyan
//    EffectMode.DynamicShadow -> Color(0xFF8B5CF6)  // violet
//    EffectMode.BlurOnScroll -> Color(0xFFFFB86B)   // warm
//    EffectMode.Glassmorphism -> Color(0xFF4ADE80)  // green-ish for contrast
//    EffectMode.Neumorphism -> Color(0xFF94A3B8)    // slate
//}
//
///* -------------------- Modifier utilities -------------------- */
//
//// One correct blur helper (Compose RenderEffect). API 31+ only.
//fun Modifier.blurLayer(radius: Dp): Modifier = composed {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//        val px = with(LocalDensity.current) { radius.toPx() }
//        this.graphicsLayer { renderEffect = BlurEffect(px, px, TileMode.Clamp) }
//    } else this
//}
//
//// Glow cached (no allocations per frame)
//fun Modifier.glow(
//    color: Color,
//    alpha: Float,
//    blurRadius: Dp,
//    cornerRadius: Dp,
//    spread: Dp = 0.dp
//): Modifier = this.drawWithCache {
//    val frameworkPaint = Paint().asFrameworkPaint().apply {
//        isAntiAlias = true
//        this.color = color.copy(alpha = alpha).toArgb()
//        maskFilter = BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL)
//    }
//
//    val s = spread.toPx()
//    val r = cornerRadius.toPx()
//
//    onDrawBehind {
//        val rect = RectF(-s, -s, size.width + s, size.height + s)
//        drawIntoCanvas { canvas ->
//            canvas.nativeCanvas.drawRoundRect(rect, r, r, frameworkPaint)
//        }
//    }
//}
//
//// Neumorphism cached (no allocations per frame)
//private fun Modifier.neumorphCached(
//    baseColor: Color,
//    cornerRadius: Dp,
//    depth: Dp,
//    pressed: Boolean
//): Modifier = this.drawWithCache {
//    val r = cornerRadius.toPx()
//    val d = depth.toPx()
//
//    val light = if (!pressed) Color.White.copy(alpha = 0.14f) else Color.Black.copy(alpha = 0.18f)
//    val dark = if (!pressed) Color.Black.copy(alpha = 0.28f) else Color.White.copy(alpha = 0.12f)
//
//    fun shadowPaint(c: Color): android.graphics.Paint {
//        return android.graphics.Paint().apply {
//            isAntiAlias = true
//            color = c.toArgb()
//            maskFilter = BlurMaskFilter(d, BlurMaskFilter.Blur.NORMAL)
//        }
//    }
//
//    val lightPaint = shadowPaint(light)
//    val darkPaint = shadowPaint(dark)
//
//    onDrawBehind {
//        // base
//        drawRoundRect(color = baseColor, cornerRadius = CornerRadius(r, r))
//
//        drawIntoCanvas { canvas ->
//            val rect = RectF(0f, 0f, size.width, size.height)
//
//            // top-left shadow
//            canvas.nativeCanvas.save()
//            canvas.nativeCanvas.translate(-d * 0.6f, -d * 0.6f)
//            canvas.nativeCanvas.drawRoundRect(rect, r, r, lightPaint)
//            canvas.nativeCanvas.restore()
//
//            // bottom-right shadow
//            canvas.nativeCanvas.save()
//            canvas.nativeCanvas.translate(d * 0.7f, d * 0.7f)
//            canvas.nativeCanvas.drawRoundRect(rect, r, r, darkPaint)
//            canvas.nativeCanvas.restore()
//        }
//    }
//}
