package com.igdtuw.ontrack.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.igdtuw.ontrack.AuthViewModel
import com.igdtuw.timetable.UploadTimetableViewModel
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadTimetableScreen(
    navController: NavController,
    viewModel: UploadTimetableViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val daysOfWeek = listOf(
        DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY
    )
    val scrollState = rememberScrollState()

    MainScaffold(
        screenTitle = "Upload Timetable",
        canNavigateBack = true,
        onNavigateBack = { navController.popBackStack() },
        onDrawerItemClick = { /* Handle drawer item clicks if needed */ },
        onLogoutClick = { /* Handle logout logic */ },
        authViewModel = authViewModel
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
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
                // Use theme color instead of hardcoded black
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                // Also fix cursor color for dark mode
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .width(120.dp)
                    .height(60.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline)
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
