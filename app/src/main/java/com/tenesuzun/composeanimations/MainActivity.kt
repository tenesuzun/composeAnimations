package com.tenesuzun.composeanimations

import android.graphics.BlurMaskFilter
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ModernTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MockSceneScreen()
                }
            }
        }
    }
}

private enum class EffectMode {
    HoverGlow, DynamicShadow, BlurOnScroll, Glassmorphism, Neumorphism
}

@Composable
fun MockSceneScreen() {
    val listState = rememberLazyListState()

    val verticalItems = remember {
        (1..5).map { i -> "Vertical card item #$i — two lines max example title goes here." }
    }
    val horizontalItems = remember {
        (1..12).map { i -> "Horizontal item #$i" }
    }

    var effectMode by remember { mutableStateOf(EffectMode.HoverGlow) }

    // 0..1 progress as you scroll a little
    val scrollProgress by remember(listState) {
        derivedStateOf {
            val px = listState.firstVisibleItemScrollOffset.toFloat()
            min(1f, px / 240f)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appBackgroundBrush())
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            GlassTopBar(
                title = "Topbar",
                progress = scrollProgress,
            )

            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp)
                ) {
                    item {
                        EffectsLab(
                            effectMode = effectMode, onSelect = { effectMode = it }, listState = listState
                        )
                    }

                    // Top 5 vertical cards
                    items(verticalItems) { title ->
                        VerticalCard(title = title)
                    }

                    // Horizontal row section
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Horizontal section",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(end = 8.dp)
                        ) {
                            items(horizontalItems) { label ->
                                HorizontalCard(label = label)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Repeat bottom 5 vertical cards
                    items(verticalItems) { title ->
                        VerticalCard(title = title)
                    }
                }
            }

            GlassBottomBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            )
        }
    }
}

/* -------------------- Top / Bottom Bars (modern, glass-ish) -------------------- */

@Composable
private fun GlassTopBar(
    title: String,
    progress: Float,
) {
    val alpha by animateFloatAsState(
        targetValue = 0.55f + 0.25f * progress, animationSpec = spring(stiffness = Spring.StiffnessMediumLow), label = "topbarAlpha"
    )

    val blurDp by animateDpAsState(
        targetValue = (6.dp + 14.dp * progress), animationSpec = spring(stiffness = Spring.StiffnessMediumLow), label = "topbarBlur"
    )

    // Not true "backdrop blur" (Compose limitation). We blur a decorative layer inside the bar
    // so you can SEE the blur animation behavior clearly.
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .shadow(12.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(18.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = alpha))
        ) {
            // Decorative “under-layer” that gets blurred as you scroll (demo purposes)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.14f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f),
                            )
                        )
                    )
                    .then(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                            Modifier.blurLayer(blurDp) else Modifier)

            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "scroll: ${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun GlassBottomBar(modifier: Modifier = Modifier) {
    Surface(
        color = Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .shadow(18.dp, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(22.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.65f))
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

/* -------------------- Cards (slightly modernized) -------------------- */

@Composable
private fun VerticalCard(title: String) {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.80f)
        ), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)), contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_photo),
                    contentDescription = "Placeholder image",
                    modifier = Modifier.size(26.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun HorizontalCard(label: String) {
    Card(
        modifier = Modifier.size(width = 160.dp, height = 190.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.80f)
        ), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)), contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_photo),
                    contentDescription = "Placeholder image",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/* -------------------- Effects Lab: see each result separately -------------------- */

@Composable
private fun EffectsLab(
    effectMode: EffectMode, onSelect: (EffectMode) -> Unit, listState: LazyListState
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Effects Lab", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(EffectMode.values().toList()) { mode ->
                EffectChip(
                    text = mode.name, selected = (mode == effectMode), onClick = { onSelect(mode) })
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

@Composable
private fun EffectChip(text: String, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
    else MaterialTheme.colorScheme.surface.copy(alpha = 0.70f)

    val border = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
    else Color.White.copy(alpha = 0.10f)

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(bg)
            .border(1.dp, border, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(
            text = text, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/* -------------------- Demo 1: Hover/Press Glow -------------------- */

@Composable
private fun DemoHoverGlow() {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    val glowAlpha by animateFloatAsState(
        targetValue = if (pressed) 0.85f else 0.25f, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "glowAlpha"
    )
    val glowRadius by animateDpAsState(
        targetValue = if (pressed) 22.dp else 10.dp, animationSpec = spring(stiffness = Spring.StiffnessLow), label = "glowRadius"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .glow(
                color = MaterialTheme.colorScheme.secondary, alpha = glowAlpha, blurRadius = glowRadius, cornerRadius = 18.dp, spread = 4.dp
            )
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.75f))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(18.dp))
            .clickable(interactionSource = interaction, indication = null) { }
            .padding(16.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Press me for glow", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(
                "On Android this is mostly “press glow”. Real hover glow needs mouse/stylus hover.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

/* -------------------- Demo 2: Dynamic Shadow (lift/settle) -------------------- */

@Composable
private fun DemoDynamicShadow() {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    val elevation by animateDpAsState(
        targetValue = if (pressed) 18.dp else 6.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = 0.75f),
        label = "dynElev"
    )
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.985f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = 0.75f),
        label = "dynScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationY = if (pressed) -1.5f else 0f
            }
            .shadow(elevation, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.78f))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(18.dp))
            .clickable(interactionSource = interaction, indication = null) { }
            .padding(16.dp)
            .drawBehind {
                // nothing — elevation handles “shadow”
            }) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Dynamic shadow (press)", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(
                "This is the clean, cheap version: animate elevation + slight scale.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

/* -------------------- Demo 3: Blur-on-scroll (visual demo) -------------------- */

@Composable
private fun DemoBlurOnScroll(listState: LazyListState) {
    val progress by remember(listState) {
        derivedStateOf { min(1f, listState.firstVisibleItemScrollOffset / 260f) }
    }
    val blurDp by animateDpAsState(
        targetValue = (2.dp + 18.dp * progress), animationSpec = spring(stiffness = Spring.StiffnessLow), label = "scrollBlur"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.70f))
    ) {
        // A colorful layer that gets blurred as you scroll
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.22f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f),
                        )
                    )
                )
                .then(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Modifier.blurLayer(blurDp) else Modifier)
        )

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Blur-on-scroll", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(
                "Scroll a bit: blur increases. Note: true backdrop blur behind other content is limited in Compose without extra tricks/libraries.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

/* -------------------- Demo 4: Glassmorphism -------------------- */

@Composable
private fun DemoGlassmorphism() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(14.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color.White.copy(alpha = 0.16f), RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.45f))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Glassmorphism", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(
                "Recipe: translucent surface + subtle border + soft shadow + optional blur layer (API 31+).",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

/* -------------------- Demo 5: Neumorphism -------------------- */

@Composable
private fun DemoNeumorphism() {
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
            .neumorph(
                baseColor = base, cornerRadius = 18.dp, depth = depth, pressed = pressed
            )
            .clip(RoundedCornerShape(18.dp))
            .clickable { pressed = !pressed }
            .padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart) {
        Text(
            text = if (pressed) "Neumorphism (pressed)" else "Neumorphism (raised)",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/* -------------------- Modern theme + background -------------------- */

@Composable
private fun ModernTheme(content: @Composable () -> Unit) {
    val scheme = darkColorScheme(
        primary = Color(0xFF8B5CF6),   // violet
        secondary = Color(0xFF22D3EE), // cyan
        tertiary = Color(0xFFFFB86B),  // warm accent
        background = Color(0xFF070A12),
        surface = Color(0xFF0E1424),
        surfaceVariant = Color(0xFF151F36),
        onBackground = Color(0xFFE7EAF0),
        onSurface = Color(0xFFE7EAF0),
        onSurfaceVariant = Color(0xFFB8C2D6),
    )

    MaterialTheme(colorScheme = scheme, content = content)
}

private fun appBackgroundBrush(): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color(0xFF070A12),
            Color(0xFF0A1020),
            Color(0xFF060913),
        )
    )
}

/* -------------------- Modifier utilities -------------------- */

fun Modifier.blurLayer(radius: Dp): Modifier = composed {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val px = with(LocalDensity.current) { radius.toPx() }
        this.graphicsLayer {
            renderEffect = BlurEffect(px, px, TileMode.Clamp)
        }
    } else this
}

@Composable
fun Modifier.graphicsBlur(radius: Dp): Modifier {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val px = radius.value // Dp -> this is not px; but RenderEffect expects px.
        // Fix: use a safe approximate conversion by treating dp as px would be wrong,
        // but good enough visually for a demo. For correctness, convert with density in draw scope.
        // We'll do the correct px conversion inside drawBehind-based approaches; here keep it simple.
        this.then(Modifier
            .drawBehind { }
            .then(
                Modifier.blurRenderEffect(radius)
            ))
    } else {
        Modifier
    }
}

@Composable
fun Modifier.blurRenderEffect(radius: Dp): Modifier {
    val density = LocalDensity.current
    val px = with(density) { radius.toPx() }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        this.graphicsLayer {
            renderEffect = BlurEffect(px, px, TileMode.Clamp)
//            renderEffect = android.graphics.RenderEffect
//                    .createBlurEffect(px, px, Shader.TileMode.CLAMP)
//                    .asComposeRenderEffect()
        }
    } else this
}


fun Modifier.glow(
    color: Color, alpha: Float, blurRadius: Dp, cornerRadius: Dp, spread: Dp = 0.dp
): Modifier = this.drawWithCache {
    val frameworkPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        this.color = color.copy(alpha = alpha).toArgb()
        maskFilter = BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL)
    }

    val s = spread.toPx()
    val r = cornerRadius.toPx()

    onDrawBehind {
        val rect = RectF(-s, -s, size.width + s, size.height + s)
        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawRoundRect(rect, r, r, frameworkPaint)
        }
    }
}

private fun Modifier.neumorph(
    baseColor: Color, cornerRadius: Dp, depth: Dp, pressed: Boolean
): Modifier = this.drawBehind {
        // Neumorphism = same base color + two opposing soft shadows.
        val r = cornerRadius.toPx()
        val d = depth.toPx()

        val light = if (!pressed) Color.White.copy(alpha = 0.14f) else Color.Black.copy(alpha = 0.18f)
        val dark = if (!pressed) Color.Black.copy(alpha = 0.28f) else Color.White.copy(alpha = 0.12f)

        fun shadowPaint(c: Color): android.graphics.Paint {
            return android.graphics.Paint().apply {
                isAntiAlias = true
                color = c.toArgb()
                maskFilter = BlurMaskFilter(d, BlurMaskFilter.Blur.NORMAL)
            }
        }

        // Base
        drawRoundRect(
            color = baseColor, cornerRadius = CornerRadius(r, r)
        )

        drawIntoCanvas { canvas ->
            val rect = RectF(0f, 0f, size.width, size.height)

            // top-left light shadow
            canvas.nativeCanvas.save()
            canvas.nativeCanvas.translate(-d * 0.6f, -d * 0.6f)
            canvas.nativeCanvas.drawRoundRect(rect, r, r, shadowPaint(light))
            canvas.nativeCanvas.restore()

            // bottom-right dark shadow
            canvas.nativeCanvas.save()
            canvas.nativeCanvas.translate(d * 0.7f, d * 0.7f)
            canvas.nativeCanvas.drawRoundRect(rect, r, r, shadowPaint(dark))
            canvas.nativeCanvas.restore()
        }
    }
