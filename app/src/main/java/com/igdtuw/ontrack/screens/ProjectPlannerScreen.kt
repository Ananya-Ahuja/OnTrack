package com.igdtuw.ontrack.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.igdtuw.ontrack.AuthViewModel
import com.igdtuw.ontrack.ProjectViewModel
import com.igdtuw.ontrack.ProjectWithMilestones

@Composable
fun ProjectPlannerScreen(
    navController: NavController,
    viewModel: ProjectViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val projects by viewModel.projects.collectAsState(initial = emptyList())

    MainScaffold(
        screenTitle = "Project Planner",
        canNavigateBack = true,
        onNavigateBack = { navController.popBackStack() },
        onDrawerItemClick = { route ->
            when (route) {
                "Home" -> navController.navigate("dashboard")
                "Calendar" -> navController.navigate("calendar")
                "To-do List" -> navController.navigate("todo")
            }
        },
        onLogoutClick = {
            authViewModel.signout()
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        },
        authViewModel = authViewModel
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (projects.isEmpty()) {
                Text(
                    "No projects yet!\nStart by adding your first project.",
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn {
                    items(projects) { projectWithMilestones ->
                        ProjectCard(
                            projectWithMilestones = projectWithMilestones,
                            onMilestoneUpdated = viewModel::updateMilestone,
                            onDelete = { viewModel.deleteProject(projectWithMilestones.project) }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Project"
            )
        }
    }

    if (showAddDialog) {
        AddProjectDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, milestones ->
                viewModel.addProject(title, milestones)
                showAddDialog = false
            }
        )
    }
}
