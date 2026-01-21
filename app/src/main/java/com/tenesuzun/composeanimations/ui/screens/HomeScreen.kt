package com.tenesuzun.composeanimations.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tenesuzun.composeanimations.ui.cards.HorizontalCard
import com.tenesuzun.composeanimations.ui.cards.VerticalCard
import com.tenesuzun.composeanimations.ui.effects.EffectMode
import com.tenesuzun.composeanimations.ui.effects.EffectsLab

@Composable
fun HomeScreen(
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    val verticalItems = remember {
        (1..5).map { i -> "Vertical card item #$i — two lines max example title goes here." }
    }
    val horizontalItems = remember {
        (1..12).map { i -> "Horizontal item #$i" }
    }

    var effectMode by remember { mutableStateOf(EffectMode.HoverGlow) }

    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 80.dp,  // TopBar için boşluk
            bottom = 100.dp // BottomBar için boşluk
        )
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
