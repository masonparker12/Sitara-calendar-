package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EventEntity
import com.example.ui.theme.PolishAmber
import com.example.ui.theme.PolishAmber50
import com.example.ui.theme.PolishAmber600
import com.example.ui.theme.PolishAmberBorder
import com.example.ui.theme.PolishIndigo
import com.example.ui.theme.PolishOrange50
import com.example.ui.theme.SitaraGradients
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.util.CalendarUtils
import com.example.util.LiveClockState
import java.time.LocalDate

@Composable
fun DateDetailsCard(
    selectedDate: LocalDate,
    today: LocalDate,
    liveClock: LiveClockState,
    events: List<EventEntity>,
    onAddEventClick: () -> Unit,
    onDeleteEvent: (EventEntity) -> Unit,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val dayName = CalendarUtils.formatDayName(selectedDate)
    val monthNameShort = CalendarUtils.formatMonthName(selectedDate).take(4)
    val dayNumber = selectedDate.dayOfMonth.toString()
    val eventCountText = when (events.size) {
        0 -> "No events scheduled"
        1 -> "1 event scheduled"
        else -> "${events.size} events scheduled"
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Banner matching exact HTML:
        // bg-linear-to-r from-amber-50 to-orange-50 rounded-3xl p-5 border border-amber-100 flex items-center gap-4
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (isDarkMode) 2.dp else 4.dp,
                    shape = RoundedCornerShape(24.dp)
                )
                .clip(RoundedCornerShape(24.dp))
                .background(
                    if (isDarkMode) SitaraGradients.AmberBannerGradientDark else SitaraGradients.AmberBannerGradient
                )
                .border(
                    1.dp,
                    if (isDarkMode) Color(0xFF78350F) else PolishAmberBorder,
                    RoundedCornerShape(24.dp)
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Amber Icon Badge: w-12 h-12 rounded-2xl bg-amber-500 shadow-md
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(PolishAmber)
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Selected Date Info",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Middle Text: label, day/date, time & event count
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "SELECTED DATE INFO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        ),
                        color = if (isDarkMode) Color(0xFFFBBF24) else PolishAmber600
                    )
                    Text(
                        text = "$dayName, $monthNameShort $dayNumber",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = if (isDarkMode) Color(0xFFFEF3C7) else Slate900
                    )
                    Text(
                        text = "${liveClock.hours12}:${liveClock.minutes} ${liveClock.amPm} • $eventCountText",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = if (isDarkMode) Color(0xFFD97706) else Slate500
                    )
                }

                // Plus action button: w-10 h-10 rounded-full bg-white shadow-sm border border-amber-100
                Box(
                    modifier = Modifier
                        .testTag("add_event_button")
                        .size(42.dp)
                        .clip(CircleShape)
                        .shadow(2.dp, CircleShape)
                        .background(if (isDarkMode) Slate900 else Color.White)
                        .border(
                            1.dp,
                            if (isDarkMode) Color(0xFF78350F) else PolishAmberBorder,
                            CircleShape
                        )
                        .clickable(onClick = onAddEventClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Event",
                        tint = if (isDarkMode) Color(0xFFFBBF24) else PolishAmber600,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Schedule & Events Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (isDarkMode) 2.dp else 4.dp,
                    shape = RoundedCornerShape(24.dp)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkMode) Slate900 else Color.White
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isDarkMode) Slate800 else Slate100
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Events Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Scheduled Activities",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = if (isDarkMode) Slate100 else Slate800
                    )

                    // Event count pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (isDarkMode) Color(0xFF1E1B4B) else Color(0xFFEEF2FF))
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${events.size} items",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = PolishIndigo
                        )
                    }
                }

                if (events.isEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isDarkMode) Slate800.copy(alpha = 0.5f) else Slate50,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isDarkMode) Slate700.copy(alpha = 0.4f) else Slate200.copy(alpha = 0.6f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp, horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = Slate400,
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                text = "No events scheduled for this day",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.sp
                                ),
                                color = if (isDarkMode) Slate400 else Slate600
                            )
                            Text(
                                text = "Tap the + button to add a meeting, task, or reminder",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp
                                ),
                                color = Slate400
                            )
                        }
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        events.forEach { event ->
                            EventItemRow(
                                event = event,
                                onDelete = { onDeleteEvent(event) },
                                isDarkMode = isDarkMode
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EventItemRow(
    event: EventEntity,
    onDelete: () -> Unit,
    isDarkMode: Boolean
) {
    val eventColor = try {
        Color(android.graphics.Color.parseColor(event.colorHex))
    } catch (_: Exception) {
        PolishIndigo
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isDarkMode) Slate800 else Slate50,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isDarkMode) Slate700 else Slate200.copy(alpha = 0.7f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Colored status indicator pill
                Box(
                    modifier = Modifier
                        .size(width = 4.dp, height = 34.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(eventColor)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        color = if (isDarkMode) Slate100 else Slate900
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = event.time,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = eventColor,
                                fontSize = 11.sp
                            )
                        )

                        if (event.description.isNotBlank()) {
                            Text(
                                text = "• " + event.description,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isDarkMode) Slate400 else Slate500,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete Event",
                    tint = if (isDarkMode) Slate400 else Slate400,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
