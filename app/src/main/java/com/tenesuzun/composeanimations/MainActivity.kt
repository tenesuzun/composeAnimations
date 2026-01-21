package com.tenesuzun.composeanimations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tenesuzun.composeanimations.ui.cards.HorizontalCard
import com.tenesuzun.composeanimations.ui.cards.VerticalCard
import com.tenesuzun.composeanimations.ui.effects.EffectsLab
import com.tenesuzun.composeanimations.ui.systembars.GlassBottomBar
import com.tenesuzun.composeanimations.ui.systembars.GlassTopBar
import com.tenesuzun.composeanimations.ui.theme.ModernTheme
import com.tenesuzun.composeanimations.ui.theme.appBackgroundBrush
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ModernTheme {
                Box(modifier = Modifier.fillMaxSize().background(brush = appBackgroundBrush()).systemBarsPadding()) {
                    MockSceneScreen()
                }
            }
        }
    }
}

enum class EffectMode {
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appBackgroundBrush())
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        val hazeState = remember { HazeState() }

        Column(modifier = Modifier
            .haze(
                hazeState,
                backgroundColor = MaterialTheme.colorScheme.background,
                tint = Color.Black.copy(alpha = .2f),
                blurRadius = 30.dp,
                )
            .fillMaxSize()
        ) {
            GlassTopBar(title = "Topbar")

            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp)
                ) {
                    item {
                        EffectsLab(
                            effectMode = effectMode,
                            onSelect = { effectMode = it },
                            listState = listState
                        )
                    }

                    items(verticalItems) { title ->
                        VerticalCard(
                            title = title,
                            effectMode = effectMode,
                        )
                    }

                    // Yatay kart
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Horizontal section",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(end = 8.dp)
                        ) {
                            items(horizontalItems) { label ->
                                HorizontalCard(
                                    label = label,
                                    effectMode = effectMode,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(verticalItems) { title ->
                        VerticalCard(
                            title = title,
                            effectMode = effectMode,
                        )
                    }
                }
            }

            GlassBottomBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars).hazeChild(state = hazeState, shape = CircleShape)
            )
        }
    }
}