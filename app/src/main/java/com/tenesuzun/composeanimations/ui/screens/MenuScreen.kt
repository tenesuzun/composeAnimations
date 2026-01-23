package com.tenesuzun.composeanimations.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tenesuzun.composeanimations.R

@Composable
fun MenuScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val blurDelta = remember {
        derivedStateOf {
            scrollState.value / 10
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .safeContentPadding()
            .systemBarsPadding()
            .padding(vertical = 25.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(color = Color.Red)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Yellow)
                    .blur(12.dp, edgeTreatment = BlurredEdgeTreatment(CircleShape))
            )
        }
        Image(
            painter = painterResource(R.drawable.wallpaper),
            contentDescription = "wallpaper",
            modifier = Modifier
                .fillMaxWidth()
                .blur(blurDelta.value.dp)
        )
    }
}