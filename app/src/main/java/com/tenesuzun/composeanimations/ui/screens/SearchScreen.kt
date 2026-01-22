package com.tenesuzun.composeanimations.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tenesuzun.composeanimations.R

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
//    // TODO: Parallax scroll buraya eklenecek
//    Box(
//        modifier = modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "Search - Parallax Scroll\n(Coming Soon)",
//            style = MaterialTheme.typography.headlineMedium,
//            color = MaterialTheme.colorScheme.onBackground
//        )
//    }
    Box(
        modifier = Modifier
            .padding(top = 100.dp, bottom = 100.dp)
    ) {
        ParallaxEffect()
    }
}

/**
 * Parallax Efekti
 *
 * https://developer.android.com/develop/ui/compose/quick-guides/content/parallax-scrolling
 */
@Composable
fun ParallaxEffect() {
    fun Modifier.parallaxLayoutModifier(scrollState: ScrollState, rate: Int) =
        layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            val height = if (rate > 0) scrollState.value / rate else scrollState.value
            layout(placeable.width, placeable.height) {
                placeable.place(0, height)
            }
        }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .systemBarsPadding()
            .safeContentPadding(),
    ) {
        Text(
            text = "Parallox Efekt",
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(horizontal = 8.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge
        )

        Image(
            painterResource(id = R.drawable.wallpaper),
            contentDescription = "Android logo",
            contentScale = ContentScale.FillWidth,
            // Reduce scrolling rate by half.
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .parallaxLayoutModifier(scrollState, 2)
        )

        Image(
            painterResource(id = R.drawable.cupcake),
            contentDescription = "Android logo",
            contentScale = ContentScale.Fit,
            // Reduce scrolling rate by half.
            modifier = Modifier.parallaxLayoutModifier(scrollState, 2)
        )

        Text(
            text = stringResource(R.string.lorem_ipsum)
                .plus(stringResource(R.string.lorem_ipsum))
                .plus(stringResource(R.string.lorem_ipsum))
                .plus(stringResource(R.string.lorem_ipsum))
                .plus(stringResource(R.string.lorem_ipsum))
                .plus(stringResource(R.string.lorem_ipsum))
                .plus(stringResource(R.string.lorem_ipsum)),
            modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 8.dp),
        )
    }
}