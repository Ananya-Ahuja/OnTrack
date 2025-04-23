package com.igdtuw.ontrack.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import com.igdtuw.ontrack.CalendarViewModel


@Composable
fun CalendarScreen(viewModel: CalendarViewModel) {
    val currentMonth by viewModel.currentMonth.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val events by viewModel.events.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        // Month navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.prevMonth() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
            }
            Text(
                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(onClick = { viewModel.nextMonth() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
            }
        }

        // Days of week header
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            DayOfWeek.entries.forEach { day ->
                Text(
                    text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        // Calendar grid
        val firstDay = currentMonth.atDay(1)
        val lastDay = currentMonth.atEndOfMonth()
        val daysInMonth = lastDay.dayOfMonth
        val firstDayOfWeek = firstDay.dayOfWeek.value % 7 // Make Sunday = 0

        val days = (1..daysInMonth).map { day -> currentMonth.atDay(day) }
        val calendarCells = List(firstDayOfWeek) { null } + days

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            items(calendarCells.size) { index ->
                val date = calendarCells[index]
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(2.dp)
                        .background(
                            if (date == selectedDate) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            else MaterialTheme.colorScheme.background
                        )
                        .clickable(enabled = date != null) {
                            date?.let { viewModel.selectDate(it) }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = date?.dayOfMonth?.toString() ?: "")
                }
            }
        }

        // Event list for selected date
        val selectedEvents = events.filter { it.date == selectedDate }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Events on ${selectedDate}:", style = MaterialTheme.typography.titleMedium)
        selectedEvents.forEach { event ->
            Text("- ${event.title}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
