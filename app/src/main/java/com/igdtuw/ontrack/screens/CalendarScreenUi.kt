package com.igdtuw.ontrack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.igdtuw.ontrack.AddTaskDialog
import com.igdtuw.ontrack.AuthViewModel
import com.igdtuw.ontrack.CalendarScreenLogic
import com.igdtuw.ontrack.TaskCard
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CalendarScreenLogic = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    MainScaffold(
        screenTitle = "Calendar",
        canNavigateBack = true,
        onNavigateBack = { navController.popBackStack() },
        onDrawerItemClick = { route ->
            when (route) {
                "Home" -> navController.navigate("dashboard")
                "To-do List" -> navController.navigate("todo")
                "ProjectPlanner" -> navController.navigate("project_planner")
                "CodeVault" -> navController.navigate("code_vault")
                "Resources" -> navController.navigate("resources")
                "CGPA Calculator" -> navController.navigate("cgpa_calculator")
            }
        },
        onLogoutClick = {
            navController.navigate("login") { popUpTo(0) { inclusive = true } }
        },
        authViewModel = authViewModel
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Safe calendar rendering with default values
            SimpleCalendar(
                selectedDate = state.selectedDate ?: LocalDate.now(),
                visibleMonth = state.visibleMonth ?: LocalDate.now(),
                onDateSelected = { date ->
                    if (date != null) viewModel.onDateSelected(date)
                },
                onMonthForward = viewModel::onMonthNavigationForward,
                onMonthBackward = viewModel::onMonthNavigationBackward
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = viewModel::toggleDialog,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Task")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tasks for ${state.selectedDate?.format(DateTimeFormatter.ISO_DATE) ?: "selected date"}:",
                style = MaterialTheme.typography.titleMedium
            )

            TaskList(
                tasks = state.currentTasks,
                onDeleteTask = viewModel::deleteTask
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate("upload_timetable") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Upload Timetable")
            }
        }
    }

    if (state.showDialog) {
        AddTaskDialog(
            selectedDate = state.selectedDate ?: LocalDate.now(),
            onDismiss = viewModel::toggleDialog,
            onSave = viewModel::addTask
        )
    }
}

@Composable
private fun SimpleCalendar(
    selectedDate: LocalDate,
    visibleMonth: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onMonthForward: () -> Unit,
    onMonthBackward: () -> Unit
) {
    val daysInMonth = remember(visibleMonth) {
        calculateDaysInMonth(visibleMonth)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        MonthNavigationHeader(
            visibleMonth = visibleMonth,
            onMonthForward = onMonthForward,
            onMonthBackward = onMonthBackward
        )

        WeekdayHeaders()

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(daysInMonth) { day ->
                CalendarDay(
                    date = day,
                    isSelected = day == selectedDate,
                    onDateSelected = onDateSelected
                )
            }
        }
    }
}

@Composable
private fun MonthNavigationHeader(
    visibleMonth: LocalDate,
    onMonthForward: () -> Unit,
    onMonthBackward: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onMonthBackward) {
            Icon(Icons.Default.ArrowBack, "Previous Month")
        }

        Text(
            text = visibleMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
            style = MaterialTheme.typography.titleLarge
        )

        IconButton(onClick = onMonthForward) {
            Icon(Icons.Default.ArrowForward, "Next Month")
        }
    }
}

@Composable
private fun WeekdayHeaders() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        DayOfWeek.values().forEach { day ->
            Text(
                text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun CalendarDay(
    date: LocalDate?,
    isSelected: Boolean,
    onDateSelected: (LocalDate) -> Unit
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Text(
        text = date?.dayOfMonth?.toString() ?: "",
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .background(backgroundColor, MaterialTheme.shapes.small)
            .clickable(enabled = date != null) { date?.let(onDateSelected) }
            .padding(8.dp),
        color = contentColor,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun TaskList(
    tasks: List<String>,
    onDeleteTask: (Int) -> Unit
) {
    if (tasks.isEmpty()) {
        Text("No tasks scheduled.", modifier = Modifier.padding(8.dp))
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        tasks.forEachIndexed { index, task ->
            TaskCard(
                task = task,
                onDelete = { onDeleteTask(index) }
            )
        }
    }
}

private fun calculateDaysInMonth(month: LocalDate): List<LocalDate?> {
    val yearMonth = YearMonth.from(month)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDay = yearMonth.atDay(1).dayOfWeek.value % 7

    return List(42) { index ->
        when {
            index < firstDay -> null
            index - firstDay >= daysInMonth -> null
            else -> yearMonth.atDay(index - firstDay + 1)
        }
    }
}
