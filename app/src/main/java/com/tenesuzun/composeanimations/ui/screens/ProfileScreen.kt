package com.tenesuzun.composeanimations.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.tenesuzun.composeanimations.R
import com.tenesuzun.composeanimations.data.SwipeCard
import com.tenesuzun.composeanimations.data.TinderCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val swipeCards = remember {
        listOf(
            SwipeCard(1, "Kartı Sağa Kaydırın", "Düzenle", "card_image_1"),
            SwipeCard(2, "Veya Sola Kaydırın", "Sil", "card_image_2"),
            SwipeCard(3, "Modern Tasarım", "Swipe to Reveal", "card_image_3"),
            SwipeCard(4, "Akıcı Animasyonlar", "Compose ile", "card_image_4"),
            SwipeCard(5, "Kolay Kullanım", "Kullanıcı Dostu", "card_image_5")
        )
    }

    val tinderCards = remember {
        listOf(
            TinderCard(1, "Ayşe", 25, "Seyahat etmeyi ve fotoğraf çekmeyi seviyorum 📸", "profile_image_1"),
            TinderCard(2, "Mehmet", 28, "Yazılım geliştirici | Kahve bağımlısı ☕", "profile_image_2"),
            TinderCard(3, "Zeynep", 24, "Yoga eğitmeni | Doğa aşığı 🌿", "profile_image_3"),
            TinderCard(4, "Can", 27, "Müzisyen | Gitar çalıyorum 🎸", "profile_image_4"),
            TinderCard(5, "Elif", 26, "Grafik tasarımcı | Sanat severim 🎨", "profile_image_5")
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Swipe to Reveal",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        items(swipeCards, key = { it.id }) { card ->
            SwipeToRevealCard(
                card = card,
                onEdit = { /* TODO: Edit action */ },
                onDelete = { /* TODO: Delete action */ }
            )
        }

        item {
            Text(
                text = "Resmi Doküman Swipe To Dismiss",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        item {
            SwipeItemExample()
        }

        item {
            Text(
                text = "Resmi Doküman Swipe To Dismiss - Animasyonlu",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        item {
            SwipeItemWithAnimationExample()
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Tinder Kartları",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        item {
            TinderCardStack(cards = tinderCards)
        }
    }
}

/**
 * https://developer.android.com/develop/ui/compose/touch-input/user-interactions/swipe-to-dismiss
 *
 * Android Developer sitesinden örnek
 */
data class TodoItem(
    val itemDescription: String,
    var isItemDone: Boolean = false
)

@Composable
fun TodoListItem(
    todoItem: TodoItem,
    onToggleDone: (TodoItem) -> Unit,
    onRemove: (TodoItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Deprecated
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.StartToEnd) onToggleDone(todoItem)
            else if (it == SwipeToDismissBoxValue.EndToStart) onRemove(todoItem)
            // Reset item when toggling done status
            it != SwipeToDismissBoxValue.StartToEnd
        }
    )

    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        initialValue = SwipeToDismissBoxValue.Settled,
        positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold
    )

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        modifier = modifier.fillMaxSize(),
        backgroundContent = {
            when (swipeToDismissBoxState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    Icon(
                        painter = if (todoItem.isItemDone) painterResource(R.drawable.ic_favorite) else painterResource(R.drawable.ic_home),
                        contentDescription = if (todoItem.isItemDone) "Done" else "Not done",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Blue)
                            .wrapContentSize(Alignment.CenterStart)
                            .padding(12.dp),
                        tint = Color.White
                    )
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "Remove item",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(12.dp),
                        tint = Color.White
                    )
                }
                SwipeToDismissBoxValue.Settled -> {}
            }
        }
    ) {
        ListItem(
            headlineContent = { Text(todoItem.itemDescription) },
            supportingContent = { Text("swipe me to update or remove.") }
        )
    }
}

@Composable
private fun SwipeItemExample() {
    val todoItems = remember {
        mutableStateListOf(
            TodoItem("Pay bills"), TodoItem("Buy groceries"),
            TodoItem("Go to gym"), TodoItem("Get dinner")
        )
    }

    LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
        items(
            items = todoItems,
            key = { it.itemDescription }
        ) { todoItem ->
            TodoListItem(
                todoItem = todoItem,
                onToggleDone = { todoItem ->
                    todoItem.isItemDone = !todoItem.isItemDone
                },
                onRemove = { todoItem ->
                    todoItems -= todoItem
                },
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
fun TodoListItemWithAnimation(
    todoItem: TodoItem,
    onToggleDone: (TodoItem) -> Unit,
    onRemove: (TodoItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
//        confirmValueChange = {
//            if (it == SwipeToDismissBoxValue.StartToEnd) onToggleDone(todoItem)
//            else if (it == SwipeToDismissBoxValue.EndToStart) onRemove(todoItem)
//            // Reset item when toggling done status
//            it != SwipeToDismissBoxValue.StartToEnd
//        }
    )

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        modifier = modifier.fillMaxSize().padding(12.dp),
        backgroundContent = {
            when (swipeToDismissBoxState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    Icon(
                        if (todoItem.isItemDone) painterResource(R.drawable.ic_favorite) else painterResource(R.drawable.ic_home),
                        contentDescription = if (todoItem.isItemDone) "Done" else "Not done",
                        modifier = Modifier
                            .fillMaxSize()
                            .drawBehind {
                                drawRect(lerp(Color.LightGray, Color.Blue, swipeToDismissBoxState.progress))
                            }
                            .wrapContentSize(Alignment.CenterStart)
                            .padding(12.dp),
                        tint = Color.White
                    )
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    Icon(
                        painter = painterResource(R.drawable.ic_favorite),
                        contentDescription = "Remove item",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(lerp(Color.LightGray, Color.Red, swipeToDismissBoxState.progress))
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(12.dp),
                        tint = Color.White
                    )
                }
                SwipeToDismissBoxValue.Settled -> {}
            }
        }
    ) {
        OutlinedCard(shape = RectangleShape) {
            ListItem(
                headlineContent = { Text(todoItem.itemDescription) },
                supportingContent = { Text("swipe me to update or remove.") }
            )
        }
    }
}

@Composable
private fun SwipeItemWithAnimationExample() {
    val todoItems = remember {
        mutableStateListOf(
            TodoItem("Pay bills"), TodoItem("Buy groceries"),
            TodoItem("Go to gym"), TodoItem("Get dinner")
        )
    }

    LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
        items(
            items = todoItems,
            key = { it.itemDescription }
        ) { todoItem ->
            TodoListItemWithAnimation(
                todoItem = todoItem,
                onToggleDone = { todoItem ->
                    todoItem.isItemDone = !todoItem.isItemDone
                },
                onRemove = { todoItem ->
                    todoItems -= todoItem
                },
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
fun SwipeToRevealCard(
    card: SwipeCard,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val revealThreshold = 120f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(100.dp)
    ) {
        // Background actions (Edit & Delete)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Edit action (left)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(120.dp)
                    .background(Color(0xFF4CAF50))
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = "Edit",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Delete action (right)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(120.dp)
                    .background(Color(0xFFF44336))
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Foreground card
        Card(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                when {
                                    offsetX.value > revealThreshold -> {
                                        offsetX.animateTo(
                                            targetValue = revealThreshold,
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessLow
                                            )
                                        )
                                    }
                                    offsetX.value < -revealThreshold -> {
                                        offsetX.animateTo(
                                            targetValue = -revealThreshold,
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessLow
                                            )
                                        )
                                    }
                                    else -> {
                                        offsetX.animateTo(
                                            targetValue = 0f,
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessMedium
                                            )
                                        )
                                    }
                                }
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            coroutineScope.launch {
                                val newOffset = (offsetX.value + dragAmount).coerceIn(-revealThreshold, revealThreshold)
                                offsetX.snapTo(newOffset)
                            }
                        }
                    )
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Placeholder for image
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = card.imageRes,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = card.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = card.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun TinderCardStack(
    cards: List<TinderCard>,
    modifier: Modifier = Modifier
) {
    var visibleCards by remember { mutableStateOf(cards) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(550.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (visibleCards.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🎉",
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tüm kartlar bitti!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            visibleCards.forEachIndexed { index, card ->
                val isTopCard = index == visibleCards.size - 1

                TinderCardItem(
                    card = card,
                    isTopCard = isTopCard,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .graphicsLayer {
                            val scale = 1f - (visibleCards.size - 1 - index) * 0.05f
                            scaleX = scale
                            scaleY = scale
                            translationY = (visibleCards.size - 1 - index) * 20f
                        },
                    onSwiped = { direction ->
                        visibleCards = visibleCards.filter { it.id != card.id }
                    }
                )
            }
        }
    }
}

@Composable
fun TinderCardItem(
    card: TinderCard,
    isTopCard: Boolean,
    modifier: Modifier = Modifier,
    onSwiped: (String) -> Unit
) {
    var offset by remember { mutableStateOf(0f) }
    var dismissRight by remember { mutableStateOf(false) }
    var dismissLeft by remember { mutableStateOf(false) }
    val density = LocalDensity.current.density
    val swipeThreshold = 300f
    val sensitivityFactor = 3f

    val rotation by animateFloatAsState(
        targetValue = offset / 50f,
        label = "rotation"
    )

    val alpha by animateFloatAsState(
        targetValue = if (dismissRight || dismissLeft) 0f else 1f,
        label = "alpha"
    )

    LaunchedEffect(dismissRight) {
        if (dismissRight) {
            delay(300)
            onSwiped("right")
            dismissRight = false
        }
    }

    LaunchedEffect(dismissLeft) {
        if (dismissLeft) {
            delay(300)
            onSwiped("left")
            dismissLeft = false
        }
    }

    Card(
        modifier = modifier
            .offset { IntOffset(offset.roundToInt(), 0) }
            .graphicsLayer {
                rotationZ = rotation
                this.alpha = alpha
            }
            .pointerInput(isTopCard) {
                if (isTopCard) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            when {
                                offset > swipeThreshold -> {
                                    dismissRight = true
                                }
                                offset < -swipeThreshold -> {
                                    dismissLeft = true
                                }
                                else -> {
                                    offset = 0f
                                }
                            }
                        }
                    ) { change, dragAmount ->
                        offset += (dragAmount / density) * sensitivityFactor
                        if (change.positionChange() != Offset.Zero) {
                            change.consume()
                        }
                    }
                }
            },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Placeholder for profile image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📸",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = card.imageRes,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 300f
                        )
                    )
            )

            // Like/Dislike indicators
            if (isTopCard && offset != 0f) {
                if (offset > 50f) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(32.dp)
                            .rotate(-20f)
                    ) {
                        Text(
                            text = "BEĞEN ❤️",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.graphicsLayer {
                                this.alpha = (offset / swipeThreshold).coerceIn(0f, 1f)
                            }
                        )
                    }
                } else if (offset < -50f) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(32.dp)
                            .rotate(20f)
                    ) {
                        Text(
                            text = "BEĞENME ✖️",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFF44336),
                            modifier = Modifier.graphicsLayer {
                                this.alpha = (abs(offset) / swipeThreshold).coerceIn(0f, 1f)
                            }
                        )
                    }
                }
            }

            // Card info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = card.name,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${card.age}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = card.bio,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}