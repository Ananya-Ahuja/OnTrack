package com.igdtuw.ontrack.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.igdtuw.ontrack.AddTaskDialog
import com.igdtuw.ontrack.CalendarScreenLogic
import com.igdtuw.ontrack.TaskCard

@Composable
fun CalendarScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CalendarScreenLogic = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = modifier.padding(16.dp)) {
        // Calendar component with proper month handling
        SimpleCalendar(
            selectedDate = state.selectedDate,
            visibleMonth = state.visibleMonth,  // Corrected here
            onDateSelected = viewModel::onDateSelected,
            onMonthForward = viewModel::onMonthNavigationForward,
            onMonthBackward = viewModel::onMonthNavigationBackward
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Add Task Button
        Button(
            onClick = viewModel::toggleDialog,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Task List Header
        Text(
            text = "Tasks for ${state.selectedDate}:",
            style = MaterialTheme.typography.titleMedium
        )

        // Task List
        TaskList(
            tasks = state.currentTasks,
            onDeleteTask = viewModel::deleteTask
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Timetable Upload Button
        Button(
            onClick = { navController.navigate("upload_timetable") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Upload Timetable")
        }
    }

    // Add Task Dialog
    if (state.showDialog) {
        AddTaskDialog(
            selectedDate = state.selectedDate,
            onDismiss = viewModel::toggleDialog,
            onSave = viewModel::addTask
        )
    }
}

@Composable
private fun TaskList(
    tasks: List<String>,
    onDeleteTask: (Int) -> Unit
) {
    if (tasks.isEmpty()) {
        Text("No tasks scheduled.")
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
