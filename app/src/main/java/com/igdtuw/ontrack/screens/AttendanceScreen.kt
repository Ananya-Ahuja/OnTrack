package com.igdtuw.ontrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.igdtuw.ontrack.AuthViewModel
import com.igdtuw.ontrack.attendance.AttendanceViewModel

@Composable
fun AttendanceScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    // Use MainScaffold for consistent layout across app
    MainScaffold(
        screenTitle = "Attendance",
        canNavigateBack = true,
        onNavigateBack = { navController.popBackStack() },
        onDrawerItemClick = { screen ->
            // Navigate to other screens based on drawer selection
            when (screen) {
                "Home" -> navController.navigate("dashboard")
                "Calendar" -> navController.navigate("calendar")
                // Handle other navigation options
            }
        },
        onLogoutClick = {
            // Handle logout
        },
        authViewModel = authViewModel
    ) { paddingValues ->
        // Main content
        AttendanceContent(
            viewModel = viewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun AttendanceContent(
    viewModel: AttendanceViewModel,
    modifier: Modifier = Modifier
) {
    val selectedDate by viewModel.selectedDate
    val selectedCourse by viewModel.selectedCourse
    val classes by viewModel.classes
    val availableCourses by viewModel.availableCourses

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Select Course Details",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Selectors for filtering
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Date selector
            DateSelector(
                selectedDate = selectedDate,
                onDateSelected = { viewModel.setSelectedDate(it) },
                modifier = Modifier.weight(1f)
            )

            // Course selector
            CourseSelector(
                courses = availableCourses,
                selectedCourse = selectedCourse,
                onCourseSelected = { viewModel.setSelectedCourse(it) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Attendance list header
        Text(
            text = "Attendance Today",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // List of classes
        if (classes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No classes scheduled for this date",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(classes) { classInfo ->
                    ClassCard(
                        classInfo = classInfo,
                        onAttended = {
                            viewModel.markAttendance(
                                classInfo.subject,
                                classInfo.professor,
                                classInfo.time,
                                classInfo.room,
                                true
                            )
                        },
                        onNotAttended = {
                            viewModel.markAttendance(
                                classInfo.subject,
                                classInfo.professor,
                                classInfo.time,
                                classInfo.room,
                                false
                            )
                        }
                    )
                }
            }
        }
    }
}
