package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PolishIndigo
import com.example.ui.theme.PolishIndigoLight
import com.example.ui.theme.PolishRose
import com.example.ui.theme.PolishRoseText
import com.example.ui.theme.SitaraGradients
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.util.CalendarDay
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

private val monthYearHeaderFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)
private val weekdayLabels = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CalendarMonthView(
    displayedYearMonth: YearMonth,
    calendarDays: List<CalendarDay>,
    onSelectDate: (LocalDate) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onTodayClick: () -> Unit,
    onOpenMonthYearPicker: () -> Unit,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Row: Month Name + Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Interactive Month and Year Title
                Row(
                    modifier = Modifier
                        .testTag("month_year_selector_button")
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onOpenMonthYearPicker)
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = displayedYearMonth.format(monthYearHeaderFormatter),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            letterSpacing = (-0.2).sp
                        ),
                        color = if (isDarkMode) Slate100 else Slate800
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select Month and Year",
                        tint = if (isDarkMode) Slate400 else Slate500,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Controls: Previous Chevron, Today pill, Next Chevron
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    IconButton(
                        onClick = onPreviousMonth,
                        modifier = Modifier
                            .testTag("prev_month_button")
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isDarkMode) Slate800 else Slate100.copy(alpha = 0.8f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous Month",
                            tint = if (isDarkMode) Slate200 else Slate600,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // "Today" Button matching HTML: bg-indigo-50 text-indigo-700 rounded-full
                    Surface(
                        modifier = Modifier
                            .testTag("today_button")
                            .clip(RoundedCornerShape(50))
                            .clickable(onClick = onTodayClick),
                        color = if (isDarkMode) Color(0xFF1E1B4B) else PolishIndigoLight,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "Today",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            ),
                            color = if (isDarkMode) Color(0xFFA5B4FC) else PolishIndigo,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }

                    IconButton(
                        onClick = onNextMonth,
                        modifier = Modifier
                            .testTag("next_month_button")
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isDarkMode) Slate800 else Slate100.copy(alpha = 0.8f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next Month",
                            tint = if (isDarkMode) Slate200 else Slate600,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Weekday Headers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                weekdayLabels.forEachIndexed { index, label ->
                    val isSunday = index == 0
                    Text(
                        text = label.uppercase(),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = if (isSunday) PolishRose else Slate400
                    )
                }
            }

            // Calendar Grid with slide animation
            AnimatedContent(
                targetState = displayedYearMonth,
                transitionSpec = {
                    if (targetState.isAfter(initialState)) {
                        slideInHorizontally(tween(250)) { width -> width } togetherWith
                                slideOutHorizontally(tween(250)) { width -> -width }
                    } else {
                        slideInHorizontally(tween(250)) { width -> -width } togetherWith
                                slideOutHorizontally(tween(250)) { width -> width }
                    }
                },
                label = "calendar_grid_animation"
            ) { _ ->
                val rows = calendarDays.chunked(7)
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rows.forEach { weekDays ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            weekDays.forEach { day ->
                                CalendarDayCell(
                                    day = day,
                                    isDarkMode = isDarkMode,
                                    onSelectDate = onSelectDate,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    day: CalendarDay,
    isDarkMode: Boolean,
    onSelectDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    val defaultTextColor = when {
        !day.isCurrentMonth -> if (isDarkMode) Slate600 else Slate300
        day.isSunday -> PolishRoseText
        else -> if (isDarkMode) Slate200 else Slate700
    }

    Box(
        modifier = modifier
            .padding(2.dp)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .then(
                    when {
                        day.isSelected -> {
                            Modifier
                                .shadow(6.dp, CircleShape)
                                .background(SitaraGradients.SelectedDateGradient)
                        }
                        day.isToday -> {
                            Modifier
                                .background(if (isDarkMode) Color(0xFF1E1B4B) else PolishIndigoLight)
                                .border(2.dp, PolishIndigo, CircleShape)
                        }
                        else -> {
                            Modifier.background(Color.Transparent)
                        }
                    }
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { onSelectDate(day.date) }
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = day.dayOfMonth.toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (day.isToday || day.isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp
                    ),
                    color = when {
                        day.isSelected -> Color.White
                        day.isToday -> if (isDarkMode) Color(0xFFA5B4FC) else PolishIndigo
                        else -> defaultTextColor
                    }
                )

                // Event Dot: matching HTML "w-1 h-1 bg-indigo-400 rounded-full"
                if (day.hasEvents) {
                    val firstColorHex = day.eventColors.firstOrNull() ?: "#4F46E5"
                    val dotColor = try {
                        Color(android.graphics.Color.parseColor(firstColorHex))
                    } catch (_: Exception) {
                        PolishIndigo
                    }

                    Box(
                        modifier = Modifier
                            .padding(top = 1.dp)
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(if (day.isSelected) Color.White else dotColor)
                    )
                }
            }
        }
    }
}
