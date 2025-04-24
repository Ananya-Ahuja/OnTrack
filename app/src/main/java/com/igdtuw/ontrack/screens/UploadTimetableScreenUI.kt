package com.igdtuw.timetable

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadTimetableScreen(
    navController: NavController,
    viewModel: UploadTimetableViewModel = hiltViewModel()
) {
    val daysOfWeek = listOf(
        DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY
    )
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Timetable") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            TimeTableGrid(
                daysOfWeek = daysOfWeek,
                viewModel = viewModel,
                scrollState = scrollState
            )

            Spacer(modifier = Modifier.height(24.dp))

            SaveButton(
                onSave = {
                    viewModel.saveTimetable(navController.context)
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun TimeTableGrid(
    daysOfWeek: List<DayOfWeek>,
    viewModel: UploadTimetableViewModel,
    scrollState: androidx.compose.foundation.ScrollState
) {
    Row(modifier = Modifier.horizontalScroll(scrollState)) {
        TimeColumnHeaders()
        daysOfWeek.forEach { day ->
            DayColumn(
                day = day,
                timeSlots = viewModel.timetable[day] ?: emptyList(),
                onTimeSlotUpdate = { hourIndex, value ->
                    viewModel.updateTimeSlot(day, hourIndex, value)
                }
            )
        }
    }
}

@Composable
private fun TimeColumnHeaders() {
    Column {
        Spacer(modifier = Modifier.height(32.dp))
        (9..17).forEach { hour ->
            Text(
                text = "$hour:00 - ${hour + 1}:00",
                modifier = Modifier
                    .height(60.dp)
                    .width(100.dp)
                    .padding(8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun DayColumn(
    day: DayOfWeek,
    timeSlots: List<String>,
    onTimeSlotUpdate: (Int, String) -> Unit
) {
    Column {
        Text(
            text = day.name,
            modifier = Modifier
                .width(120.dp)
                .padding(8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        timeSlots.forEachIndexed { hourIndex, currentValue ->
            var text by remember { mutableStateOf(TextFieldValue(currentValue)) }

            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                    onTimeSlotUpdate(hourIndex, it.text)
                },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                modifier = Modifier
                    .width(120.dp)
                    .height(60.dp)
                    .border(1.dp, Color.Gray)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun SaveButton(onSave: () -> Unit) {
    Button(
        onClick = onSave,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Save Timetable")
    }
}