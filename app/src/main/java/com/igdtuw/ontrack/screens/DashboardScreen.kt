package com.igdtuw.ontrack.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.igdtuw.ontrack.AuthViewModel

@Composable
fun DashboardScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    MainScaffold(
        screenTitle = "Home",
        canNavigateBack = false,
        onNavigateBack = { navController.popBackStack() },
        onDrawerItemClick = { route ->
            when (route) {
                "To-do List" -> navController.navigate("todo")
                "Calendar" -> navController.navigate("calendar")
                "CGPA Calculator" -> navController.navigate("cgpa_calculator")
                // Add other cases for different drawer items""
            }
        },
        onLogoutClick = {
            // Handle logout logic
            authViewModel.signout()
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        },
        authViewModel = authViewModel
    ) { paddingValues ->  // Padding from Scaffold
        Column(modifier = modifier
            .padding(paddingValues)
            .padding(16.dp)) {
            Button(
                onClick = { navController.navigate("attendance") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("View Attendance")
            }
        }
    }
}
