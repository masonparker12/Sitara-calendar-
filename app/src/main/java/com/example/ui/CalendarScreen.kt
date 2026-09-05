package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddEventDialog
import com.example.ui.components.CalendarMonthView
import com.example.ui.components.DateDetailsCard
import com.example.ui.components.MonthYearPickerDialog
import com.example.ui.components.TopHeader

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    modifier: Modifier = Modifier
) {
    val today by viewModel.today.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val displayedYearMonth by viewModel.displayedYearMonth.collectAsStateWithLifecycle()
    val liveClock by viewModel.liveClock.collectAsStateWithLifecycle()
    val calendarDays by viewModel.calendarDays.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val isAddEventDialogOpen by viewModel.isAddEventDialogOpen.collectAsStateWithLifecycle()
    val isMonthYearPickerOpen by viewModel.isMonthYearPickerOpen.collectAsStateWithLifecycle()
    val selectedDateEvents by viewModel.selectedDateEvents.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("calendar_screen_root"),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val isWideScreen = maxWidth >= 720.dp

            if (isWideScreen) {
                // Tablet / Desktop 2-column layout
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopHeader(
                        today = today,
                        liveClock = liveClock,
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { viewModel.toggleDarkMode() },
                        selectedTab = selectedTab,
                        onSelectTab = { viewModel.selectTab(it) },
                        modifier = Modifier.widthIn(max = 1200.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 1200.dp)
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Left: Monthly Calendar Grid
                        Box(
                            modifier = Modifier
                                .weight(1.15f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            CalendarMonthView(
                                displayedYearMonth = displayedYearMonth,
                                calendarDays = calendarDays,
                                onSelectDate = { date -> viewModel.selectDate(date) },
                                onPreviousMonth = { viewModel.goToPreviousMonth() },
                                onNextMonth = { viewModel.goToNextMonth() },
                                onTodayClick = { viewModel.goToToday() },
                                onOpenMonthYearPicker = { viewModel.openMonthYearPicker() },
                                isDarkMode = isDarkMode
                            )
                        }

                        // Right: Selected Date Details + Events
                        Box(
                            modifier = Modifier
                                .weight(0.95f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            DateDetailsCard(
                                selectedDate = selectedDate,
                                today = today,
                                liveClock = liveClock,
                                events = selectedDateEvents,
                                onAddEventClick = { viewModel.openAddEventDialog() },
                                onDeleteEvent = { event -> viewModel.deleteEvent(event) },
                                isDarkMode = isDarkMode
                            )
                        }
                    }
                }
            } else {
                // Mobile Portrait Layout
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopHeader(
                        today = today,
                        liveClock = liveClock,
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { viewModel.toggleDarkMode() },
                        selectedTab = selectedTab,
                        onSelectTab = { viewModel.selectTab(it) }
                    )

                    CalendarMonthView(
                        displayedYearMonth = displayedYearMonth,
                        calendarDays = calendarDays,
                        onSelectDate = { date -> viewModel.selectDate(date) },
                        onPreviousMonth = { viewModel.goToPreviousMonth() },
                        onNextMonth = { viewModel.goToNextMonth() },
                        onTodayClick = { viewModel.goToToday() },
                        onOpenMonthYearPicker = { viewModel.openMonthYearPicker() },
                        isDarkMode = isDarkMode
                    )

                    DateDetailsCard(
                        selectedDate = selectedDate,
                        today = today,
                        liveClock = liveClock,
                        events = selectedDateEvents,
                        onAddEventClick = { viewModel.openAddEventDialog() },
                        onDeleteEvent = { event -> viewModel.deleteEvent(event) },
                        isDarkMode = isDarkMode
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    // Add Event Modal Dialog
    if (isAddEventDialogOpen) {
        AddEventDialog(
            selectedDate = selectedDate,
            onDismiss = { viewModel.closeAddEventDialog() },
            onConfirm = { title, time, description, colorHex ->
                viewModel.addEvent(title, time, description, colorHex)
            }
        )
    }

    // Month & Year Picker Modal Dialog
    if (isMonthYearPickerOpen) {
        MonthYearPickerDialog(
            currentYearMonth = displayedYearMonth,
            onDismiss = { viewModel.closeMonthYearPicker() },
            onConfirm = { year, month ->
                viewModel.setYearMonth(year, month)
                viewModel.closeMonthYearPicker()
            }
        )
    }
}
