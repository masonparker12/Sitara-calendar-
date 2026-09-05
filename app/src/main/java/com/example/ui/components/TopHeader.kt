package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PolishIndigo
import com.example.ui.theme.SitaraGradients
import com.example.util.CalendarUtils
import com.example.util.LiveClockState
import java.time.LocalDate

enum class CalendarTabMode {
    CALENDAR_VIEW,
    UPCOMING,
    TASKS
}

@Composable
fun TopHeader(
    today: LocalDate,
    liveClock: LiveClockState,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    selectedTab: CalendarTabMode = CalendarTabMode.CALENDAR_VIEW,
    onSelectTab: (CalendarTabMode) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    val currentDayName = CalendarUtils.formatDayName(today)
    val formattedDate = CalendarUtils.formatShortDate(today)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
            ),
        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isDarkMode) SitaraGradients.HeaderGradientDark else SitaraGradients.HeaderGradient
                )
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Top Row: Title, Date, Live Clock & Theme toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Left Column: Sitara Calendar label, Date, Day
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "SITARA CALENDAR",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                letterSpacing = 2.sp
                            ),
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                letterSpacing = (-0.5).sp
                            ),
                            color = Color.White
                        )
                        Text(
                            text = currentDayName,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            ),
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    // Right Column: Glassmorphism Live Clock & Mode Toggle
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Glassmorphism Clock Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.22f))
                                .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                // Pulsing live indicator dot
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .alpha(pulseAlpha)
                                )
                                Spacer(modifier = Modifier.width(2.dp))

                                // Monospace time display
                                Text(
                                    text = "${liveClock.hours12}:${liveClock.minutes}:${liveClock.seconds}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = Color.White
                                )

                                Text(
                                    text = liveClock.amPm.lowercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = Color.White.copy(alpha = 0.85f),
                                    modifier = Modifier.padding(start = 2.dp)
                                )
                            }
                        }

                        // Compact Dark Mode Button
                        IconButton(
                            onClick = onToggleDarkMode,
                            modifier = Modifier
                                .testTag("dark_mode_toggle")
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                                .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                        ) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Filter / View Pills Row matching the Professional Polish HTML:
                // buttons: Upcoming, Calendar View (active), Tasks
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TabPill(
                        label = "Calendar View",
                        isSelected = selectedTab == CalendarTabMode.CALENDAR_VIEW,
                        onClick = { onSelectTab(CalendarTabMode.CALENDAR_VIEW) }
                    )
                    TabPill(
                        label = "Upcoming",
                        isSelected = selectedTab == CalendarTabMode.UPCOMING,
                        onClick = { onSelectTab(CalendarTabMode.UPCOMING) }
                    )
                    TabPill(
                        label = "Tasks",
                        isSelected = selectedTab == CalendarTabMode.TASKS,
                        onClick = { onSelectTab(CalendarTabMode.TASKS) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TabPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .then(
                if (isSelected) {
                    Modifier
                        .shadow(4.dp, RoundedCornerShape(50))
                        .background(Color.White)
                } else {
                    Modifier
                        .background(Color.White.copy(alpha = 0.22f))
                        .border(1.dp, Color.White.copy(alpha = 0.28f), RoundedCornerShape(50))
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 12.sp
            ),
            color = if (isSelected) PolishIndigo else Color.White
        )
    }
}
