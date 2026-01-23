package com.tenesuzun.composeanimations.ui.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tenesuzun.composeanimations.R
import com.tenesuzun.composeanimations.ui.effects.EffectCardShell
import com.tenesuzun.composeanimations.ui.effects.EffectMode

@Composable
fun VerticalCard(
    title: String,
    effectMode: EffectMode,
    scrollOffset: Float = 0f,
    itemIndex: Int = 0,
    firstVisibleIndex: Int = 0
) {
    val shape = RoundedCornerShape(16.dp)

    // MenuScreen mantığı: scrollOffset'i blur değerine çevir
    // Scroll arttıkça blur artar (üstteki kartlar için)
    val blurAmount = if (effectMode == EffectMode.BlurOnScroll) {
        val distanceFromTop = (itemIndex - firstVisibleIndex).coerceAtLeast(0)
        when (distanceFromTop) {
            0 -> (scrollOffset / 15f).coerceIn(0f, 20f).dp  // İlk kart - en çok blur
            1 -> (scrollOffset / 25f).coerceIn(0f, 12f).dp  // İkinci kart
            2 -> (scrollOffset / 40f).coerceIn(0f, 8f).dp   // Üçüncü kart
            else -> 0.dp
        }
    } else 0.dp

    // BlurOnScroll için özel render - arka plan image ile
    if (effectMode == EffectMode.BlurOnScroll) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .blur(blurAmount)
        ) {
            // Arka plan image
            Image(
                painter = painterResource(R.drawable.wallpaper),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.3f
            )

            // İçerik
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.92f)),
                    contentAlignment = Alignment.Center
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
    } else {
        // Diğer efektler için EffectCardShell kullan
        EffectCardShell(
            effectMode = effectMode,
            shape = shape,
            modifier = Modifier.fillMaxWidth(),
            scrollOffset = scrollOffset,
            itemIndex = itemIndex,
            firstVisibleIndex = firstVisibleIndex
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.92f)),
                    contentAlignment = Alignment.Center
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
}