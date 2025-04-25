package com.igdtuw.ontrack.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.igdtuw.ontrack.AuthViewModel

@Composable
fun AppScreen(
    title: String,
    showBackButton: Boolean = false,
    authViewModel: AuthViewModel = viewModel(),
    onBackPressed: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    // Remember current screen is title
    val currentScreen = remember { title }

    MainScaffold(
        screenTitle = currentScreen,
        canNavigateBack = showBackButton,
        onNavigateBack = onBackPressed,
        onDrawerItemClick = { selectedScreen ->
            // Handle navigation here or through a NavHostController
        },
        onLogoutClick = {
            // Handle logout
        },
        authViewModel = authViewModel
    ) { paddingValues ->
        // Apply padding to content
        content(paddingValues)
    }
}
