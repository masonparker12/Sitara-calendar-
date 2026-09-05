package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.EventEntity
import com.example.data.EventRepository
import com.example.ui.components.CalendarTabMode
import com.example.util.CalendarDay
import com.example.util.CalendarUtils
import com.example.util.LiveClockState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

class CalendarViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventRepository

    init {
        val database = AppDatabase.getInstance(application)
        repository = EventRepository(database.eventDao())
    }

    private val _today = MutableStateFlow(LocalDate.now())
    val today: StateFlow<LocalDate> = _today.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _displayedYearMonth = MutableStateFlow(YearMonth.now())
    val displayedYearMonth: StateFlow<YearMonth> = _displayedYearMonth.asStateFlow()

    private val _liveClock = MutableStateFlow(CalendarUtils.getLiveClockState())
    val liveClock: StateFlow<LiveClockState> = _liveClock.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isAddEventDialogOpen = MutableStateFlow(false)
    val isAddEventDialogOpen: StateFlow<Boolean> = _isAddEventDialogOpen.asStateFlow()

    private val _isMonthYearPickerOpen = MutableStateFlow(false)
    val isMonthYearPickerOpen: StateFlow<Boolean> = _isMonthYearPickerOpen.asStateFlow()

    private val _selectedTab = MutableStateFlow(CalendarTabMode.CALENDAR_VIEW)
    val selectedTab: StateFlow<CalendarTabMode> = _selectedTab.asStateFlow()

    // All events flow from Room
    val allEvents: StateFlow<List<EventEntity>> = repository.getAllEvents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Events mapped by date for calendar dot indicators
    val eventsByDate: StateFlow<Map<String, List<String>>> = allEvents
        .combine(_today) { events, _ ->
            events.groupBy { it.date }
                .mapValues { entry -> entry.value.map { it.colorHex } }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // Events for the selected date
    val selectedDateEvents: StateFlow<List<EventEntity>> = combine(allEvents, _selectedDate) { events, selDate ->
        val dateKey = CalendarUtils.formatDateKey(selDate)
        events.filter { it.date == dateKey }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Calendar grid cells
    val calendarDays: StateFlow<List<CalendarDay>> = combine(
        _displayedYearMonth,
        _today,
        _selectedDate,
        eventsByDate
    ) { yearMonth, todayDate, selDate, eventMap ->
        CalendarUtils.generateCalendarDays(yearMonth, todayDate, selDate, eventMap)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Live clock loop updating every 1000ms
        viewModelScope.launch {
            while (isActive) {
                _liveClock.value = CalendarUtils.getLiveClockState(LocalTime.now())
                // Check if date changed at midnight
                val currentDate = LocalDate.now()
                if (currentDate != _today.value) {
                    _today.value = currentDate
                }
                delay(1000L)
            }
        }
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        // If user clicks a date outside the current month (leading or trailing days),
        // smoothly transition the month to that date's month
        val dateYearMonth = YearMonth.from(date)
        if (dateYearMonth != _displayedYearMonth.value) {
            _displayedYearMonth.value = dateYearMonth
        }
    }

    fun goToNextMonth() {
        _displayedYearMonth.value = _displayedYearMonth.value.plusMonths(1)
    }

    fun goToPreviousMonth() {
        _displayedYearMonth.value = _displayedYearMonth.value.minusMonths(1)
    }

    fun goToToday() {
        val now = LocalDate.now()
        _today.value = now
        _selectedDate.value = now
        _displayedYearMonth.value = YearMonth.from(now)
    }

    fun setYearMonth(year: Int, month: Int) {
        _displayedYearMonth.value = YearMonth.of(year, month)
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setDarkMode(dark: Boolean) {
        _isDarkMode.value = dark
    }

    fun selectTab(tab: CalendarTabMode) {
        _selectedTab.value = tab
    }

    fun openAddEventDialog() {
        _isAddEventDialogOpen.value = true
    }

    fun closeAddEventDialog() {
        _isAddEventDialogOpen.value = false
    }

    fun openMonthYearPicker() {
        _isMonthYearPickerOpen.value = true
    }

    fun closeMonthYearPicker() {
        _isMonthYearPickerOpen.value = false
    }

    fun addEvent(title: String, time: String, description: String, colorHex: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val dateKey = CalendarUtils.formatDateKey(_selectedDate.value)
            val newEvent = EventEntity(
                title = title.trim(),
                date = dateKey,
                time = time.ifBlank { "All Day" },
                description = description.trim(),
                colorHex = colorHex
            )
            repository.insertEvent(newEvent)
            _isAddEventDialogOpen.value = false
        }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.deleteEvent(event)
        }
    }
}
