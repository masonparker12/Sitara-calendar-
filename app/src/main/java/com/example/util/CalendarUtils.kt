package com.example.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

data class CalendarDay(
    val date: LocalDate,
    val dayOfMonth: Int,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val isSelected: Boolean,
    val isSunday: Boolean,
    val hasEvents: Boolean = false,
    val eventColors: List<String> = emptyList()
)

data class LiveClockState(
    val hours12: String = "12",
    val minutes: String = "00",
    val seconds: String = "00",
    val amPm: String = "AM",
    val fullFormattedTime: String = "12:00:00 AM"
)

object CalendarUtils {
    private val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH)
    private val monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH)
    private val fullDateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH)
    private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.ENGLISH)
    val standardDateKeyFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)

    fun getLiveClockState(time: LocalTime = LocalTime.now()): LiveClockState {
        val hourVal = time.hour
        val hour12 = when {
            hourVal == 0 -> 12
            hourVal > 12 -> hourVal - 12
            else -> hourVal
        }
        val hoursStr = String.format(Locale.ENGLISH, "%02d", hour12)
        val minutesStr = String.format(Locale.ENGLISH, "%02d", time.minute)
        val secondsStr = String.format(Locale.ENGLISH, "%02d", time.second)
        val amPmStr = if (hourVal >= 12) "PM" else "AM"
        val fullTime = time.format(timeFormatter)

        return LiveClockState(
            hours12 = hoursStr,
            minutes = minutesStr,
            seconds = secondsStr,
            amPm = amPmStr,
            fullFormattedTime = fullTime
        )
    }

    fun formatDayName(date: LocalDate): String = date.format(dayOfWeekFormatter)
    fun formatMonthName(date: LocalDate): String = date.format(monthFormatter)
    fun formatFullDate(date: LocalDate): String = date.format(fullDateFormatter)
    fun formatShortDate(date: LocalDate): String = date.format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH))
    fun formatYear(date: LocalDate): String = date.year.toString()
    fun formatDateKey(date: LocalDate): String = date.format(standardDateKeyFormatter)

    fun getRelativeDayDescription(selectedDate: LocalDate, today: LocalDate): String {
        return when {
            selectedDate == today -> "Today"
            selectedDate == today.plusDays(1) -> "Tomorrow"
            selectedDate == today.minusDays(1) -> "Yesterday"
            selectedDate.isAfter(today) -> "In ${ChronoUnit.DAYS.between(today, selectedDate)} days"
            else -> "${ChronoUnit.DAYS.between(selectedDate, today)} days ago"
        }
    }

    fun generateCalendarDays(
        yearMonth: YearMonth,
        today: LocalDate,
        selectedDate: LocalDate,
        eventsByDate: Map<String, List<String>>
    ): List<CalendarDay> {
        val days = mutableListOf<CalendarDay>()

        val firstDayOfMonth = yearMonth.atDay(1)
        val lengthOfMonth = yearMonth.lengthOfMonth()

        // Sunday = 0, Monday = 1, ..., Saturday = 6
        val firstDayIndex = firstDayOfMonth.dayOfWeek.value % 7

        // Previous month padding days
        val prevYearMonth = yearMonth.minusMonths(1)
        val prevMonthDays = prevYearMonth.lengthOfMonth()
        for (dayNum in (prevMonthDays - firstDayIndex + 1)..prevMonthDays) {
            val date = prevYearMonth.atDay(dayNum)
            val dateKey = date.format(standardDateKeyFormatter)
            val colors = eventsByDate[dateKey] ?: emptyList()
            days.add(
                CalendarDay(
                    date = date,
                    dayOfMonth = dayNum,
                    isCurrentMonth = false,
                    isToday = date == today,
                    isSelected = date == selectedDate,
                    isSunday = date.dayOfWeek == DayOfWeek.SUNDAY,
                    hasEvents = colors.isNotEmpty(),
                    eventColors = colors
                )
            )
        }

        // Current month days
        for (dayNum in 1..lengthOfMonth) {
            val date = yearMonth.atDay(dayNum)
            val dateKey = date.format(standardDateKeyFormatter)
            val colors = eventsByDate[dateKey] ?: emptyList()
            days.add(
                CalendarDay(
                    date = date,
                    dayOfMonth = dayNum,
                    isCurrentMonth = true,
                    isToday = date == today,
                    isSelected = date == selectedDate,
                    isSunday = date.dayOfWeek == DayOfWeek.SUNDAY,
                    hasEvents = colors.isNotEmpty(),
                    eventColors = colors
                )
            )
        }

        // Next month padding days to round to complete rows
        val totalSoFar = days.size
        val targetCells = if (totalSoFar <= 35) 35 else 42
        val trailingCount = targetCells - totalSoFar
        val nextYearMonth = yearMonth.plusMonths(1)
        for (dayNum in 1..trailingCount) {
            val date = nextYearMonth.atDay(dayNum)
            val dateKey = date.format(standardDateKeyFormatter)
            val colors = eventsByDate[dateKey] ?: emptyList()
            days.add(
                CalendarDay(
                    date = date,
                    dayOfMonth = dayNum,
                    isCurrentMonth = false,
                    isToday = date == today,
                    isSelected = date == selectedDate,
                    isSunday = date.dayOfWeek == DayOfWeek.SUNDAY,
                    hasEvents = colors.isNotEmpty(),
                    eventColors = colors
                )
            )
        }

        return days
    }
}
