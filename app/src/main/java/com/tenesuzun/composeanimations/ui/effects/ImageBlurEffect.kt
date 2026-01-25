package com.tenesuzun.composeanimations.ui.effects

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tenesuzun.composeanimations.R

@Composable
fun ImageBlurEffect(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    val blurDelta = remember {
        derivedStateOf {
            scrollState.value / 10
        }
    }

    Image(
        painter = painterResource(R.drawable.wallpaper),
        contentDescription = "wallpaper",
        modifier = Modifier
            .fillMaxWidth()
            .blur(blurDelta.value.dp)
    )
}