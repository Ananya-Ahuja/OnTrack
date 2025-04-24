package com.igdtuw.ontrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun SimpleCalendar(
    selectedDate: LocalDate,
    visibleMonth: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onMonthForward: () -> Unit,
    onMonthBackward: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        MonthNavigationHeader(
            visibleMonth = visibleMonth,
            onMonthForward = onMonthForward,
            onMonthBackward = onMonthBackward
        )

        Spacer(modifier = Modifier.height(8.dp))
        WeekdayHeaders()
        Spacer(modifier = Modifier.height(4.dp))
        CalendarGrid(
            selectedDate = selectedDate,
            visibleMonth = visibleMonth,
            onDateSelected = onDateSelected
        )
    }
}

@Composable
private fun MonthNavigationHeader(
    visibleMonth: LocalDate,
    onMonthForward: () -> Unit,
    onMonthBackward: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onMonthBackward) {
            Text("◀")
        }

        Text(
            text = "${visibleMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${visibleMonth.year}",
            style = MaterialTheme.typography.titleLarge
        )

        IconButton(onClick = onMonthForward) {
            Text("▶")
        }
    }
}

@Composable
private fun WeekdayHeaders() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    selectedDate: LocalDate,
    visibleMonth: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysInMonth = visibleMonth.lengthOfMonth()
    val firstDayOfWeek = visibleMonth.withDayOfMonth(1).dayOfWeek.value % 7
    val weeks = (daysInMonth + firstDayOfWeek + 6) / 7 // Calculate weeks needed

    Column {
        var dayCounter = 1
        repeat(weeks) { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (dayIndex in 0..6) {
                    val position = week * 7 + dayIndex
                    if (position < firstDayOfWeek || dayCounter > daysInMonth) {
                        SpacerCell(modifier = Modifier.weight(1f))
                    } else {
                        CalendarDayCell(
                            day = dayCounter,
                            date = visibleMonth.withDayOfMonth(dayCounter),
                            isSelected = visibleMonth.withDayOfMonth(dayCounter) == selectedDate,
                            onDateSelected = onDateSelected,
                            modifier = Modifier.weight(1f)
                        )
                        dayCounter++
                    }
                }
            }
        }
    }
}

@Composable
private fun SpacerCell(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(40.dp)
    )
}

@Composable
private fun CalendarDayCell(
    day: Int,
    date: LocalDate,
    isSelected: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = { onDateSelected(date) },
        modifier = modifier
            .padding(4.dp)
            .height(40.dp)
            .wrapContentHeight(Alignment.CenterVertically)
    ) {
        Text(
            text = day.toString(),
            color = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}