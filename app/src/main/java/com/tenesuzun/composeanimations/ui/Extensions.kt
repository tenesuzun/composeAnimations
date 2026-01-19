package com.tenesuzun.composeanimations.ui

import android.graphics.BlurMaskFilter
import android.graphics.RectF
import android.os.Build
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// One correct blur helper (Compose RenderEffect). API 31+ only.
fun Modifier.blurLayer(radius: Dp): Modifier = composed {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val px = with(LocalDensity.current) { radius.toPx() }
        this.graphicsLayer { renderEffect = BlurEffect(px, px, TileMode.Clamp) }
    } else this
}

fun Modifier.glow(
    color: Color,
    alpha: Float,
    blurRadius: Dp,
    cornerRadius: Dp,
    spread: Dp = 0.dp
): Modifier = this.drawWithCache {
    val blurPx = blurRadius.toPx()
    val mask = if (blurPx > 0.5f) BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL) else null

    val frameworkPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        this.color = color.copy(alpha = alpha).toArgb()
        maskFilter = mask
    }

    val s = spread.toPx()
    val r = cornerRadius.toPx()

    onDrawBehind {
        if (alpha <= 0f || blurPx <= 0.5f) return@onDrawBehind // nothing to draw safely
        val rect = RectF(-s, -s, size.width + s, size.height + s)
        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawRoundRect(rect, r, r, frameworkPaint)
        }
    }
}

fun Modifier.neumorphCached(
    baseColor: Color,
    cornerRadius: Dp,
    depth: Dp,
    pressed: Boolean
): Modifier = this.drawWithCache {
    val r = cornerRadius.toPx()
    val d = depth.toPx()

    // Always draw base; shadows only if d is meaningful
    val canBlur = d > 0.5f

    val light = if (!pressed) Color.White.copy(alpha = 0.14f) else Color.Black.copy(alpha = 0.18f)
    val dark  = if (!pressed) Color.Black.copy(alpha = 0.28f) else Color.White.copy(alpha = 0.12f)

    fun shadowPaint(c: Color): android.graphics.Paint =
        android.graphics.Paint().apply {
            isAntiAlias = true
            color = c.toArgb()
            maskFilter = if (canBlur) BlurMaskFilter(d, BlurMaskFilter.Blur.NORMAL) else null
        }

    val lightPaint = if (canBlur) shadowPaint(light) else null
    val darkPaint  = if (canBlur) shadowPaint(dark) else null

    onDrawBehind {
        drawRoundRect(color = baseColor, cornerRadius = CornerRadius(r, r))

        if (!canBlur || lightPaint == null || darkPaint == null) return@onDrawBehind

        drawIntoCanvas { canvas ->
            val rect = RectF(0f, 0f, size.width, size.height)

            canvas.nativeCanvas.save()
            canvas.nativeCanvas.translate(-d * 0.6f, -d * 0.6f)
            canvas.nativeCanvas.drawRoundRect(rect, r, r, lightPaint)
            canvas.nativeCanvas.restore()

            canvas.nativeCanvas.save()
            canvas.nativeCanvas.translate(d * 0.7f, d * 0.7f)
            canvas.nativeCanvas.drawRoundRect(rect, r, r, darkPaint)
            canvas.nativeCanvas.restore()
        }
    }
}