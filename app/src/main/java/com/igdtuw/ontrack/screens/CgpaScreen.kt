package com.igdtuw.ontrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.igdtuw.ontrack.AuthViewModel
import com.igdtuw.ontrack.CgpaViewModel
import com.igdtuw.ontrack.Course
import com.igdtuw.ontrack.R


@Composable
fun CgpaScreen(
    navController: NavController,
    viewModel: CgpaViewModel = hiltViewModel(),
    authViewModel: AuthViewModel
) {
    val courses by viewModel.courses.collectAsState(initial = emptyList())
    val cgpa by viewModel.cgpa.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(courses) {
        viewModel.calculateCgpa(courses)
    }

    MainScaffold(
        screenTitle = "CGPA Calculator",
        canNavigateBack = true,
        onNavigateBack = { navController.popBackStack() },
        onDrawerItemClick = { /* Handle navigation */ },
        onLogoutClick = { /* Handle logout */ },
        authViewModel = authViewModel
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // CGPA Display
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Current CGPA",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "%.2f".format(cgpa),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Course List
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(courses) { course ->
                    CourseItem(
                        course = course,
                        onUpdate = viewModel::updateCourse,
                        onDelete = viewModel::deleteCourse
                    )
                }
            }

            // Add Course Button
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, "Add Course")
            }
        }
    }

    if (showAddDialog) {
        AddCourseDialog(
            onDismiss = { showAddDialog = false },
            onSave = { course ->
                viewModel.addCourse(course)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun CourseItem(
    course: Course,
    onUpdate: (Course) -> Unit,
    onDelete: (Course) -> Unit
) {
    var name by remember { mutableStateOf(course.name) }
    var credits by remember { mutableStateOf(course.credits.toString()) }
    var grade by remember { mutableStateOf(course.grade) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Course Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row {
                    OutlinedTextField(
                        value = credits,
                        onValueChange = { if (it.matches(Regex("^\\d*\$"))) credits = it },
                        label = { Text("Credits") },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    GradeDropdown(
                        selectedGrade = grade,
                        onGradeSelected = { grade = it }
                    )
                }
            }

            IconButton(onClick = {
                onUpdate(course.copy(name = name, credits = credits.toInt(), grade = grade))
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_save),
                    contentDescription = "Save"
                )
            }

            IconButton(onClick = { onDelete(course) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun GradeDropdown(
    selectedGrade: String,
    onGradeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val grades = listOf("S", "A", "B", "C", "D", "E", "F")

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedGrade)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            grades.forEach { grade ->
                DropdownMenuItem(
                    text = { Text(grade) },
                    onClick = {
                        onGradeSelected(grade)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AddCourseDialog(
    onDismiss: () -> Unit,
    onSave: (Course) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var credits by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf("S") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Course") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Course Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = credits,
                    onValueChange = { if (it.matches(Regex("^\\d*\$"))) credits = it },
                    label = { Text("Credits") },
                    modifier = Modifier.fillMaxWidth()
                )

                GradeDropdown(
                    selectedGrade = grade,
                    onGradeSelected = { grade = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && credits.isNotBlank()) {
                        onSave(
                            Course(
                                name = name,
                                credits = credits.toInt(),
                                grade = grade
                            )
                        )
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
