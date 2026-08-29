package com.project.zephyr.client.overlay.kitsugui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.zephyr.client.R
import com.project.zephyr.client.ui.theme.KitsuPrimary
import com.project.zephyr.client.ui.theme.KitsuSurface
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LogcatCategoryUi() {
    val modernFont = FontFamily(
        Font(R.font.fredoka_light)
    )

    var isAutoScroll by remember { mutableStateOf(true) }
    var selectedFilter by remember { mutableStateOf<LogLevel?>(null) }

    val logs = remember { mutableStateOf<List<LogEntry>>(emptyList()) }
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        while (true) {
            val filteredLogs = if (selectedFilter != null) {
                LogcatBuffer.getLogs().filter { it.level == selectedFilter }
            } else {
                LogcatBuffer.getLogs()
            }
            logs.value = filteredLogs
            if (isAutoScroll && logs.value.isNotEmpty()) {
                listState.animateScrollToItem(logs.value.size - 1)
            }
            delay(100)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                label = "All",
                isSelected = selectedFilter == null,
                color = KitsuPrimary,
                onClick = { selectedFilter = null }
            )
            FilterChip(
                label = "Debug",
                isSelected = selectedFilter == LogLevel.DEBUG,
                color = LogLevel.DEBUG.color,
                onClick = { selectedFilter = LogLevel.DEBUG }
            )
            FilterChip(
                label = "Info",
                isSelected = selectedFilter == LogLevel.INFO,
                color = LogLevel.INFO.color,
                onClick = { selectedFilter = LogLevel.INFO }
            )
            FilterChip(
                label = "Warning",
                isSelected = selectedFilter == LogLevel.WARNING,
                color = LogLevel.WARNING.color,
                onClick = { selectedFilter = LogLevel.WARNING }
            )
            FilterChip(
                label = "Error",
                isSelected = selectedFilter == LogLevel.ERROR,
                color = LogLevel.ERROR.color,
                onClick = { selectedFilter = LogLevel.ERROR }
            )

            Spacer(modifier = Modifier.weight(1f))

            AutoScrollToggle(
                isEnabled = isAutoScroll,
                onToggle = { isAutoScroll = it }
            )

            ClearButton(
                onClick = { LogcatBuffer.clear() }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0D0D0D)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0D0D0D),
                                Color(0xFF151515)
                            )
                        )
                    )
            ) {
                if (logs.value.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No logs yet...",
                            color = Color(0xFF4B5563),
                            fontSize = 14.sp,
                            fontFamily = modernFont
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(logs.value) { entry ->
                            LogEntryItem(
                                entry = entry,
                                fontFamily = modernFont
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${logs.value.size} entries",
                color = Color(0xFF6B7280),
                fontSize = 11.sp,
                fontFamily = modernFont
            )
            Text(
                text = "Zephyr Logcat",
                color = Color(0xFF374151),
                fontSize = 11.sp,
                fontFamily = modernFont
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) color.copy(alpha = 0.2f) else Color(0xFF1F1F23),
        animationSpec = tween(150),
        label = "chipBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) color else Color(0xFF374151),
        animationSpec = tween(150),
        label = "chipBorder"
    )

    Box(
        modifier = Modifier
            .height(28.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) color else Color(0xFF9CA3AF),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun AutoScrollToggle(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isEnabled) KitsuPrimary.copy(alpha = 0.2f) else Color(0xFF1F1F23),
        animationSpec = tween(150),
        label = "scrollBg"
    )

    Box(
        modifier = Modifier
            .height(28.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = if (isEnabled) KitsuPrimary else Color(0xFF374151),
                shape = RoundedCornerShape(8.dp)
            )
            .background(backgroundColor)
            .clickable { onToggle(!isEnabled) }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = ir.alirezaivaz.tablericons.R.drawable.ic_refresh),
                contentDescription = "Auto Scroll",
                tint = if (isEnabled) KitsuPrimary else Color(0xFF9CA3AF),
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = "Auto",
                color = if (isEnabled) KitsuPrimary else Color(0xFF9CA3AF),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ClearButton(onClick: () -> Unit) {
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100),
        label = "clearScale"
    )

    Box(
        modifier = Modifier
            .size(28.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(Color(0xFFEF4444).copy(alpha = 0.2f))
            .border(
                width = 1.dp,
                color = Color(0xFFEF4444).copy(alpha = 0.5f),
                shape = CircleShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = ir.alirezaivaz.tablericons.R.drawable.ic_trash),
            contentDescription = "Clear",
            tint = Color(0xFFEF4444),
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
private fun LogEntryItem(
    entry: LogEntry,
    fontFamily: FontFamily
) {
    val dateFormat = remember { SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFF161616))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(entry.level.color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = entry.level.symbol,
                color = entry.level.color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = dateFormat.format(Date(entry.timestamp)),
            color = Color(0xFF4B5563),
            fontSize = 10.sp,
            fontFamily = fontFamily
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "[${entry.tag}]",
            color = entry.level.color,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = fontFamily,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 120.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = entry.message,
            color = Color(0xFFE5E7EB),
            fontSize = 10.sp,
            fontFamily = fontFamily,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
