package com.igdtuw.ontrack

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.igdtuw.ontrack.screens.*
import com.igdtuw.ontrack.screens.UploadTimetableScreen
import com.igdtuw.timetable.UploadTimetableViewModel

@Composable
fun Navigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            Login(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable("signup") {
            Signup(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable("home") {
            DashboardScreen(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // Add Calendar screen route
        composable("calendar") {
            val viewModel: CalendarScreenLogic = hiltViewModel()
            CalendarScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("upload_timetable") {
            val viewModel: UploadTimetableViewModel = hiltViewModel()
            UploadTimetableScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("attendance") {
            AttendanceScreen(navController = navController)
        }

        composable("todo") {
            val viewModel: TodoViewModel = hiltViewModel()
            TodoScreen(
                navController = navController,
                viewModel = viewModel,
                authViewModel = authViewModel
            )
        }
    }
}
