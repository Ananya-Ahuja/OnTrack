package com.igdtuw.ontrack

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.igdtuw.ontrack.screens.Login
import com.igdtuw.ontrack.screens.Signup
import com.igdtuw.ontrack.screens.Home

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
            Signup(modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable("home") {
            Home(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }
    }
}