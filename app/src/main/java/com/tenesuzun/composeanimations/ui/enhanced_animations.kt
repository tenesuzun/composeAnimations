//package com.tenesuzun.composeanimations.ui
//
//import android.graphics.BlurMaskFilter
//import android.graphics.RectF
//import android.os.Build
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.animation.core.*
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.slideInVertically
//import androidx.compose.animation.slideOutVertically
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.interaction.collectIsPressedAsState
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyListState
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.composed
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.drawBehind
//import androidx.compose.ui.draw.drawWithCache
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.geometry.CornerRadius
//import androidx.compose.ui.graphics.*
//import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
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
//private enum class EffectMode { HoverGlow, DynamicShadow, BlurOnScroll, Glassmorphism, Neumorphism }
//private enum class BottomBarStyle { Sliding, Morphing, Bouncing, Fade, Scale }
//
//@Composable
//fun MockSceneScreen() {
//    val listState = rememberLazyListState()
//    var selectedTab by remember { mutableStateOf(0) }
//    var bottomBarStyle by remember { mutableStateOf(BottomBarStyle.Sliding) }
//    var effectMode by remember { mutableStateOf(_root_ide_package_.com.tenesuzun.composeanimations.EffectMode.HoverGlow) }
//
//    val verticalItems = remember { (1..5).map { i -> "Card #$i — Press to see effect" } }
//    val horizontalItems = remember { (1..12).map { i -> "Item #$i" } }
//
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
//            GlassTopBar(title = "Effects Lab", progress = scrollProgress)
//
//            Box(modifier = Modifier.weight(1f)) {
//                LazyColumn(
//                    state = listState,
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.spacedBy(12.dp),
//                    contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 100.dp)
//                ) {
//                    item { EffectsLab(effectMode = effectMode, onSelect = { effectMode = it }, listState = listState) }
//                    item { BottomBarStyleSelector(bottomBarStyle) { bottomBarStyle = it } }
//                    items(verticalItems) { title -> VerticalCard(title = title, effectMode = effectMode) }
//                    item {
//                        Spacer(Modifier.height(8.dp))
//                        Text("Horizontal Section", style = MaterialTheme.typography.titleMedium,
//                            color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(bottom = 8.dp))
//                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(end = 8.dp)) {
//                            items(horizontalItems) { label -> HorizontalCard(label = label, effectMode = effectMode) }
//                        }
//                    }
//                    items(verticalItems) { title -> VerticalCard(title = title, effectMode = effectMode) }
//                }
//            }
//
//            AnimatedBottomBar(
//                selectedTab = selectedTab,
//                onTabSelected = { selectedTab = it },
//                style = bottomBarStyle,
//                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
//            )
//        }
//    }
//}
//
///* ===== BOTTOM BAR ===== */
//
//data class BottomNavItem(val label: String, val icon: Int)
//
//@Composable
//private fun AnimatedBottomBar(selectedTab: Int, onTabSelected: (Int) -> Unit, style: BottomBarStyle, modifier: Modifier = Modifier) {
//    val tabs = listOf(
//        _root_ide_package_.com.tenesuzun.composeanimations.ui.BottomNavItem(
//            "Home",
//            _root_ide_package_.com.tenesuzun.composeanimations.R.drawable.ic_home
//        ),
//        _root_ide_package_.com.tenesuzun.composeanimations.ui.BottomNavItem(
//            "Search",
//            _root_ide_package_.com.tenesuzun.composeanimations.R.drawable.ic_search
//        ),
//        _root_ide_package_.com.tenesuzun.composeanimations.ui.BottomNavItem(
//            "Fav",
//            _root_ide_package_.com.tenesuzun.composeanimations.R.drawable.ic_favorite
//        ),
//        _root_ide_package_.com.tenesuzun.composeanimations.ui.BottomNavItem(
//            "User",
//            _root_ide_package_.com.tenesuzun.composeanimations.R.drawable.ic_person
//        )
//    )
//
//    Surface(
//        color = Color.Transparent,
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(14.dp, 12.dp)
//            .shadow(18.dp, RoundedCornerShape(22.dp))
//            .clip(RoundedCornerShape(22.dp))
//            .border(1.dp, Color.White.copy(0.10f), RoundedCornerShape(22.dp))
//    ) {
//        Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface.copy(0.75f)).padding(8.dp)) {
//            when (style) {
//                BottomBarStyle.Sliding -> SlidingIndicator(selectedTab)
//                BottomBarStyle.Morphing -> MorphingIndicator(selectedTab)
//                BottomBarStyle.Bouncing -> BouncingIndicator(selectedTab)
//                BottomBarStyle.Fade -> FadeIndicator(selectedTab)
//                BottomBarStyle.Scale -> ScaleIndicator(selectedTab)
//            }
//
//            Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
//                tabs.forEachIndexed { index, item ->
//                    BottomNavButton(item, selectedTab == index, { onTabSelected(index) }, style)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun BottomNavButton(item: BottomNavItem, selected: Boolean, onClick: () -> Unit, style: BottomBarStyle) {
//    val iconScale by animateFloatAsState(
//        if (selected) when(style) {
//            BottomBarStyle.Scale -> 1.3f
//            BottomBarStyle.Bouncing -> 1.2f
//            else -> 1.15f
//        } else 1f,
//        spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow), "iconScale"
//    )
//
//    val iconColor by animateColorAsState(
//        if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(0.5f),
//        tween(300), "iconColor"
//    )
//
//    Column(
//        Modifier.clip(CircleShape).clickable(onClick = onClick).padding(16.dp, 10.dp),
//        Alignment.CenterHorizontally, Arrangement.Center
//    ) {
//        Icon(
//            painterResource(item.icon), item.label,
//            Modifier.size(26.dp).graphicsLayer { scaleX = iconScale; scaleY = iconScale },
//            tint = iconColor
//        )
//
//        AnimatedVisibility(
//            visible = selected && style != BottomBarStyle.Scale,
//            enter = fadeIn() + slideInVertically { it / 2 },
//            exit = fadeOut() + slideOutVertically { it / 2 }
//        ) {
//            Text(item.label, style = MaterialTheme.typography.labelSmall,
//                color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 4.dp))
//        }
//    }
//}
//
//@Composable
//private fun BoxScope.SlidingIndicator(selectedTab: Int) {
//    val offsetX by animateDpAsState((selectedTab * 90).dp, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium), "slide")
//    Box(Modifier.offset(offsetX).width(80.dp).height(48.dp).clip(RoundedCornerShape(24.dp))
//        .background(Brush.horizontalGradient(listOf(
//            MaterialTheme.colorScheme.primary.copy(0.35f),
//            MaterialTheme.colorScheme.secondary.copy(0.35f)
//        ))))
//}
//
//@Composable
//private fun BoxScope.MorphingIndicator(selectedTab: Int) {
//    val offsetX by animateDpAsState((selectedTab * 90).dp, spring(Spring.StiffnessMedium), "morph")
//    val width by animateDpAsState(85.dp, spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow), "width")
//    Box(Modifier.offset(offsetX).width(width).height(48.dp).clip(RoundedCornerShape(24.dp))
//        .background(Brush.radialGradient(listOf(
//            MaterialTheme.colorScheme.tertiary.copy(0.45f),
//            MaterialTheme.colorScheme.primary.copy(0.3f)
//        ))))
//}
//
//@Composable
//private fun BoxScope.BouncingIndicator(selectedTab: Int) {
//    val offsetX by animateDpAsState((selectedTab * 90).dp, spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMedium), "bounce")
//    Box(Modifier.offset(offsetX, (-4).dp).width(80.dp).height(52.dp).clip(RoundedCornerShape(26.dp))
//        .background(MaterialTheme.colorScheme.secondary.copy(0.4f))
//        .border(2.dp, MaterialTheme.colorScheme.secondary.copy(0.6f), RoundedCornerShape(26.dp)))
//}
//
//@Composable
//private fun BoxScope.FadeIndicator(selectedTab: Int) {
//    val offsetX by animateDpAsState((selectedTab * 90).dp, tween(400, easing = FastOutSlowInEasing), "fade")
//    val alpha by animateFloatAsState(0.45f, tween(300), "alpha")
//    Box(Modifier.offset(offsetX).width(80.dp).height(48.dp).clip(RoundedCornerShape(24.dp))
//        .background(MaterialTheme.colorScheme.primary.copy(alpha)))
//}
//
//@Composable
//private fun BoxScope.ScaleIndicator(selectedTab: Int) {
//    val offsetX by animateDpAsState((selectedTab * 90).dp, spring(Spring.StiffnessMedium), "scaleOff")
//    val scale by animateFloatAsState(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow), "scale")
//    Box(Modifier.offset(offsetX).width(80.dp).height(48.dp).graphicsLayer { scaleX = scale; scaleY = scale }
//        .clip(CircleShape).background(Brush.linearGradient(listOf(
//            MaterialTheme.colorScheme.tertiary.copy(0.35f),
//            MaterialTheme.colorScheme.secondary.copy(0.35f)
//        ))))
//}
//
//@Composable
//private fun BottomBarStyleSelector(currentStyle: BottomBarStyle, onStyleSelected: (BottomBarStyle) -> Unit) {
//    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
//        Text("Bottom Bar Animation", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
//        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
//            items(BottomBarStyle.entries) { style ->
//                EffectChip(style.name, style == currentStyle) { onStyleSelected(style) }
//            }
//        }
//    }
//}
//
///* ===== TOP BAR ===== */
//
//@Composable
//private fun GlassTopBar(title: String, progress: Float) {
//    val alpha by animateFloatAsState(0.55f + 0.25f * progress, spring(Spring.StiffnessMediumLow), "alpha")
//    val blurDp by animateDpAsState(6.dp + 14.dp * progress, spring(Spring.StiffnessMediumLow), "blur")
//
//    Surface(
//        Color.Transparent,
//        Modifier.fillMaxWidth().padding(14.dp, 10.dp).shadow(12.dp, RoundedCornerShape(18.dp))
//            .clip(RoundedCornerShape(18.dp)).border(1.dp, Color.White.copy(0.10f), RoundedCornerShape(18.dp))
//    ) {
//        Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface.copy(alpha))) {
//            Box(Modifier.fillMaxWidth().height(56.dp)
//                .background(Brush.linearGradient(listOf(
//                    MaterialTheme.colorScheme.primary.copy(0.20f),
//                    MaterialTheme.colorScheme.tertiary.copy(0.14f),
//                    MaterialTheme.colorScheme.secondary.copy(0.18f)
//                )))
//                .then(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Modifier.blurLayer(blurDp) else Modifier))
//
//            Row(Modifier.fillMaxWidth().padding(16.dp, 14.dp), verticalAlignment = Alignment.CenterVertically) {
//                Text(title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
//                Spacer(Modifier.weight(1f))
//                Text("scroll: ${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelMedium,
//                    color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
//            }
//        }
//    }
//}
//
///* ===== CARDS ===== */
//
//@Composable
//private fun VerticalCard(title: String, effectMode: com.tenesuzun.composeanimations.EffectMode) {
//    val interaction = remember { MutableInteractionSource() }
//    val pressed by interaction.collectIsPressedAsState()
//
//    Card(
//        Modifier.fillMaxWidth().then(getCardModifier(effectMode, pressed)),
//        RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(getCardColor(effectMode)),
//        elevation = CardDefaults.cardElevation(if (effectMode == _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.DynamicShadow && pressed) 18.dp else 2.dp)
//    ) {
//        Row(Modifier.fillMaxWidth().clickable(interaction, null) {}.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
//            Box(Modifier.size(56.dp).clip(RoundedCornerShape(14.dp))
//                .background(Brush.linearGradient(listOf(
//                    MaterialTheme.colorScheme.primary.copy(0.35f),
//                    MaterialTheme.colorScheme.secondary.copy(0.35f)
//                ))), Alignment.Center) {
//                Icon(painterResource(_root_ide_package_.com.tenesuzun.composeanimations.R.drawable.ic_photo), null, Modifier.size(26.dp), MaterialTheme.colorScheme.primary)
//            }
//            Spacer(Modifier.size(12.dp))
//            Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface,
//                maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth())
//        }
//    }
//}
//
//@Composable
//private fun HorizontalCard(label: String, effectMode: com.tenesuzun.composeanimations.EffectMode) {
//    val interaction = remember { MutableInteractionSource() }
//    val pressed by interaction.collectIsPressedAsState()
//
//    Card(
//        Modifier.size(160.dp, 190.dp).then(getCardModifier(effectMode, pressed)),
//        RoundedCornerShape(18.dp),
//        colors = CardDefaults.cardColors(getCardColor(effectMode)),
//        elevation = CardDefaults.cardElevation(if (effectMode == _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.DynamicShadow && pressed) 18.dp else 2.dp)
//    ) {
//        Column(Modifier.fillMaxSize().clickable(interaction, null) {}.padding(14.dp), Arrangement.spacedBy(10.dp)) {
//            Box(Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(16.dp))
//                .background(Brush.radialGradient(listOf(
//                    MaterialTheme.colorScheme.tertiary.copy(0.45f),
//                    MaterialTheme.colorScheme.secondary.copy(0.25f)
//                ))), Alignment.Center) {
//                Icon(painterResource(_root_ide_package_.com.tenesuzun.composeanimations.R.drawable.ic_photo), null, Modifier.size(30.dp), MaterialTheme.colorScheme.tertiary)
//            }
//            Text(label, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface,
//                maxLines = 2, overflow = TextOverflow.Ellipsis)
//        }
//    }
//}
//
//@Composable
//private fun getCardModifier(effectMode: com.tenesuzun.composeanimations.EffectMode, pressed: Boolean): Modifier {
//    val glowAlpha by animateFloatAsState(if (pressed && effectMode == _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.HoverGlow) 0.85f else 0.25f,
//        spring(Spring.StiffnessLow), "glow")
//    val scale by animateFloatAsState(if (pressed && effectMode == _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.DynamicShadow) 0.98f else 1f,
//        spring(Spring.StiffnessMediumLow), "scale")
//
//    return when (effectMode) {
//        _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.HoverGlow -> Modifier.glow(Color(0xFF22D3EE), glowAlpha, 20.dp, 16.dp, 3.dp)
//            .border(1.5.dp, Color(0xFF22D3EE).copy(glowAlpha * 0.7f), RoundedCornerShape(16.dp))
//        _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.DynamicShadow -> Modifier.graphicsLayer { scaleX = scale; scaleY = scale }
//        _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.Glassmorphism -> Modifier.border(1.5.dp, Color.White.copy(0.25f), RoundedCornerShape(16.dp))
//            .shadow(10.dp, RoundedCornerShape(16.dp))
//        _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.Neumorphism -> Modifier.neumorph(
//            MaterialTheme.colorScheme.surface.copy(0.92f), 16.dp, if (pressed) 4.dp else 10.dp, pressed)
//        else -> Modifier
//    }
//}
//
//@Composable
//private fun getCardColor(effectMode: com.tenesuzun.composeanimations.EffectMode) = when (effectMode) {
//    _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.HoverGlow -> MaterialTheme.colorScheme.surface.copy(0.88f)
//    _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.DynamicShadow -> MaterialTheme.colorScheme.surface.copy(0.92f)
//    _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.Glassmorphism -> MaterialTheme.colorScheme.surface.copy(0.50f)
//    _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.Neumorphism -> MaterialTheme.colorScheme.surface.copy(0.95f)
//    else -> MaterialTheme.colorScheme.surface.copy(0.80f)
//}
//
///* ===== EFFECTS LAB ===== */
//
//@Composable
//private fun EffectsLab(effectMode: com.tenesuzun.composeanimations.EffectMode, onSelect: (com.tenesuzun.composeanimations.EffectMode) -> Unit, listState: LazyListState) {
//    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//        Text("Card Effect Mode", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
//        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
//            items(_root_ide_package_.com.tenesuzun.composeanimations.EffectMode.entries) { mode -> EffectChip(mode.name, mode == effectMode) { onSelect(mode) } }
//        }
//        when (effectMode) {
//            _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.HoverGlow -> DemoHoverGlow()
//            _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.DynamicShadow -> DemoDynamicShadow()
//            _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.BlurOnScroll -> DemoBlurOnScroll(listState)
//            _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.Glassmorphism -> DemoGlassmorphism()
//            _root_ide_package_.com.tenesuzun.composeanimations.EffectMode.Neumorphism -> DemoNeumorphism()
//        }
//    }
//}
//
//@Composable
//private fun EffectChip(text: String, selected: Boolean, onClick: () -> Unit) {
//    val bg = if (selected) Brush.linearGradient(listOf(
//        MaterialTheme.colorScheme.primary.copy(0.45f),
//        MaterialTheme.colorScheme.secondary.copy(0.35f)
//    )) else Brush.linearGradient(listOf(
//        MaterialTheme.colorScheme.surface.copy(0.70f),
//        MaterialTheme.colorScheme.surface.copy(0.70f)
//    ))
//    val border = if (selected) MaterialTheme.colorScheme.primary.copy(0.8f) else Color.White.copy(0.10f)
//
//    Box(Modifier.clip(CircleShape).background(bg).border(1.5.dp, border, CircleShape)
//        .clickable(onClick = onClick).padding(16.dp, 10.dp)) {
//        Text(text, style = MaterialTheme.typography.labelLarge,
//            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface)
//    }
//}
//
//@Composable
//private fun DemoHoverGlow() {
//    val interaction = remember { MutableInteractionSource() }
//    val pressed by interaction.collectIsPressedAsState()
//    val glowAlpha by animateFloatAsState(if (pressed) 0.9f else 0.3f, spring(Spring.StiffnessLow), "glow")
//    val glowRadius by animateDpAsState(if (pressed) 24.dp else 12.dp, spring(Spring.StiffnessLow), "radius")
//
//    Box(Modifier.fillMaxWidth().glow(Color(0xFF22D3EE), glowAlpha, glowRadius, 18.dp, 4.dp)
//        .clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surface.copy(0.78f))
//        .border(1.5.dp, Color(0xFF22D3EE).copy(glowAlpha * 0.7f), RoundedCornerShape(18.dp))
//        .clickable(interaction, null) {}.padding(16.dp)) {
//        Column(Arrangement.spacedBy(6.dp)) {
//            Text("Press for Glow Effect", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
//            Text("Cyan glow animates on press. Now applied to all cards!", style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
//        }
//    }
//}
//
//@Composable
//private fun DemoDynamicShadow() {
//    val interaction = remember { MutableInteractionSource() }
//    val pressed by interaction.collectIsPressedAsState()
//    val elevation by animateDpAsState(if (pressed) 20.dp else 6.dp, spring(Spring.StiffnessMediumLow, 0.75f), "elev")
//    val scale by animateFloatAsState(if (pressed) 0.98f else 1f, spring(Spring.StiffnessMediumLow, 0.75f), "scale")
//
//    Box(Modifier.fillMaxWidth().graphicsLayer { scaleX = scale; scaleY = scale; translationY = if (pressed) -2f else 0f }
//        .shadow(elevation, RoundedCornerShape(18.dp)).clip(RoundedCornerShape(18.dp))
//        .background(MaterialTheme.colorScheme.surface.copy(0.82f))
//        .border(1.dp, Color.White.copy(0.10f), RoundedCornerShape(18.dp))
//        .clickable(interaction, null) {}.padding(16.dp)) {
//        Column(Arrangement.spacedBy(6.dp)) {
//            Text("Dynamic Shadow (Press)", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
//            Text("Elevation increases & card lifts on press. Clean & performant!", style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
//        }
//    }
//}
//
//@Composable
//private fun DemoBlurOnScroll(listState: LazyListState) {
//    val progress by remember(listState) { derivedStateOf { min(1f, listState.firstVisibleItemScrollOffset / 260f) } }
//    val blurDp by animateDpAsState(2.dp + 20.dp * progress, spring(Spring.StiffnessLow), "blur")
//
//    Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp))
//        .border(1.dp, Color.White.copy(0.10f), RoundedCornerShape(18.dp))
//        .background(MaterialTheme.colorScheme.surface.copy(0.72f))) {
//        Box(Modifier.matchParentSize().background(Brush.linearGradient(listOf(
//            MaterialTheme.colorScheme.tertiary.copy(0.28f),
//            MaterialTheme.colorScheme.primary.copy(0.22f),
//            MaterialTheme.colorScheme.secondary.copy(0.22f)
//        ))).then(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Modifier.blurLayer(blurDp) else Modifier))
//
//        Column(Modifier.padding(16.dp), Arrangement.spacedBy(6.dp)) {
//            Text("Blur-on-Scroll", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
//            Text("Scroll increases blur. Progress: ${(progress * 100).toInt()}%",
//                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
//        }
//    }
//}
//
//@Composable
//private fun DemoGlassmorphism() {
//    Box(Modifier.fillMaxWidth().shadow(16.dp, RoundedCornerShape(20.dp)).clip(RoundedCornerShape(20.dp))
//        .border(1.5.dp, Color.White.copy(0.20f), RoundedCornerShape(20.dp))
//        .background(MaterialTheme.colorScheme.surface.copy(0.42f)).padding(16.dp)) {
//        Column(Arrangement.spacedBy(6.dp)) {
//            Text("Glassmorphism", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
//            Text("Translucent surface + borders + shadow. Modern & elegant!",
//                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
//        }
//    }
//}
//
//@Composable
//private fun DemoNeumorphism() {
//    var pressed by remember { mutableStateOf(false) }
//    val depth by animateDpAsState(if (pressed) 3.dp else 12.dp, spring(Spring.StiffnessMediumLow, 0.8f), "depth")
//    val base = MaterialTheme.colorScheme.surface.copy(0.94f)
//
//    Box(Modifier.fillMaxWidth().height(74.dp).neumorph(base, 18.dp, depth, pressed)
//        .clip(RoundedCornerShape(18.dp)).clickable { pressed = !pressed }.padding(horizontal = 16.dp),
//        Alignment.CenterStart) {
//        Text(if (pressed) "Neumorphism (Pressed)" else "Neumorphism (Raised)",
//            style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
//    }
//}
//
///* ===== THEME & UTILS ===== */
//
//@Composable
//private fun ModernTheme(content: @Composable () -> Unit) {
//    val scheme = darkColorScheme(
//        primary = Color(0xFF8B5CF6), secondary = Color(0xFF22D3EE), tertiary = Color(0xFFFFB86B),
//        background = Color(0xFF070A12), surface = Color(0xFF0E1424), surfaceVariant = Color(0xFF151F36),
//        onBackground = Color(0xFFE7EAF0), onSurface = Color(0xFFE7EAF0), onSurfaceVariant = Color(0xFFB8C2D6)
//    )
//    MaterialTheme(colorScheme = scheme, content = content)
//}
//
//private fun appBackgroundBrush() = Brush.linearGradient(listOf(Color(0xFF070A12), Color(0xFF0A1020), Color(0xFF060913)))
//
//fun Modifier.blurLayer(radius: Dp): Modifier = composed {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//        val px = with(LocalDensity.current) { radius.toPx() }
//        graphicsLayer { renderEffect = BlurEffect(px, px, TileMode.Clamp) }
//    } else this
//}
//
//fun Modifier.glow(color: Color, alpha: Float, blurRadius: Dp, cornerRadius: Dp, spread: Dp = 0.dp): Modifier = drawWithCache {
//    val paint = Paint().asFrameworkPaint().apply {
//        isAntiAlias = true
//        this.color = color.copy(alpha).toArgb()
//        maskFilter = BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.