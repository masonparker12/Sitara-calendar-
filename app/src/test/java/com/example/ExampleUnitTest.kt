package com.example

import com.example.util.CalendarUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

class ExampleUnitTest {

  @Test
  fun testLeapYearDays() {
    // 2024 is a leap year (February has 29 days)
    val feb2024 = YearMonth.of(2024, 2)
    assertEquals(29, feb2024.lengthOfMonth())

    // 2026 is not a leap year (February has 28 days)
    val feb2026 = YearMonth.of(2026, 2)
    assertEquals(28, feb2026.lengthOfMonth())

    // 2000 was a leap year (divisible by 400)
    val feb2000 = YearMonth.of(2000, 2)
    assertEquals(29, feb2000.lengthOfMonth())

    // 1900 was not a leap year (divisible by 100 but not 400)
    val feb1900 = YearMonth.of(1900, 2)
    assertEquals(28, feb1900.lengthOfMonth())
  }

  @Test
  fun testMonthDayCounts() {
    assertEquals(31, YearMonth.of(2026, 1).lengthOfMonth())
    assertEquals(28, YearMonth.of(2026, 2).lengthOfMonth())
    assertEquals(31, YearMonth.of(2026, 3).lengthOfMonth())
    assertEquals(30, YearMonth.of(2026, 4).lengthOfMonth())
    assertEquals(31, YearMonth.of(2026, 5).lengthOfMonth())
    assertEquals(30, YearMonth.of(2026, 6).lengthOfMonth())
    assertEquals(31, YearMonth.of(2026, 7).lengthOfMonth())
    assertEquals(31, YearMonth.of(2026, 8).lengthOfMonth())
    assertEquals(30, YearMonth.of(2026, 9).lengthOfMonth())
    assertEquals(31, YearMonth.of(2026, 10).lengthOfMonth())
    assertEquals(30, YearMonth.of(2026, 11).lengthOfMonth())
    assertEquals(31, YearMonth.of(2026, 12).lengthOfMonth())
  }

  @Test
  fun testDayOfWeekPositioning() {
    // 2026-09-01 is a Tuesday
    val sep1_2026 = LocalDate.of(2026, 9, 1)
    assertEquals(DayOfWeek.TUESDAY, sep1_2026.dayOfWeek)
    // Sunday-first index: Sunday = 0, Monday = 1, Tuesday = 2
    val sundayFirstIndex = sep1_2026.dayOfWeek.value % 7
    assertEquals(2, sundayFirstIndex)
  }

  @Test
  fun testGenerateCalendarDays() {
    val today = LocalDate.of(2026, 9, 5) // Saturday
    val ym = YearMonth.of(2026, 9)

    val days = CalendarUtils.generateCalendarDays(
      yearMonth = ym,
      today = today,
      selectedDate = today,
      eventsByDate = mapOf("2026-09-05" to listOf("#8E24AA"))
    )

    // Should contain a multiple of 7 cells (35 or 42)
    assertTrue(days.size % 7 == 0)
    assertTrue(days.size == 35 || days.size == 42)

    // First two days should be previous month padding (Aug 30, Aug 31)
    assertFalse(days[0].isCurrentMonth)
    assertEquals(30, days[0].dayOfMonth)
    assertFalse(days[1].isCurrentMonth)
    assertEquals(31, days[1].dayOfMonth)

    // Index 2 should be Sep 1 (Current Month)
    assertTrue(days[2].isCurrentMonth)
    assertEquals(1, days[2].dayOfMonth)

    // Index 6 should be Sep 5 (Saturday - today and selected)
    val todayCell = days.first { it.date == today }
    assertTrue(todayCell.isToday)
    assertTrue(todayCell.isSelected)
    assertTrue(todayCell.hasEvents)
    assertEquals(listOf("#8E24AA"), todayCell.eventColors)
  }

  @Test
  fun testRelativeDayDescription() {
    val today = LocalDate.of(2026, 9, 5)

    assertEquals("Today", CalendarUtils.getRelativeDayDescription(today, today))
    assertEquals("Tomorrow", CalendarUtils.getRelativeDayDescription(today.plusDays(1), today))
    assertEquals("Yesterday", CalendarUtils.getRelativeDayDescription(today.minusDays(1), today))
    assertEquals("In 5 days", CalendarUtils.getRelativeDayDescription(today.plusDays(5), today))
    assertEquals("3 days ago", CalendarUtils.getRelativeDayDescription(today.minusDays(3), today))
  }

  @Test
  fun testLiveClockFormatting() {
    val morningTime = LocalTime.of(9, 5, 8)
    val morningState = CalendarUtils.getLiveClockState(morningTime)
    assertEquals("09", morningState.hours12)
    assertEquals("05", morningState.minutes)
    assertEquals("08", morningState.seconds)
    assertEquals("AM", morningState.amPm)

    val afternoonTime = LocalTime.of(14, 30, 45)
    val afternoonState = CalendarUtils.getLiveClockState(afternoonTime)
    assertEquals("02", afternoonState.hours12)
    assertEquals("30", afternoonState.minutes)
    assertEquals("45", afternoonState.seconds)
    assertEquals("PM", afternoonState.amPm)

    val midnightTime = LocalTime.of(0, 0, 0)
    val midnightState = CalendarUtils.getLiveClockState(midnightTime)
    assertEquals("12", midnightState.hours12)
    assertEquals("AM", midnightState.amPm)

    val noonTime = LocalTime.of(12, 0, 0)
    val noonState = CalendarUtils.getLiveClockState(noonTime)
    assertEquals("12", noonState.hours12)
    assertEquals("PM", noonState.amPm)
  }
}
