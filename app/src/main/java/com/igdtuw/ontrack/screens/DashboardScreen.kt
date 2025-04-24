package com.igdtuw.ontrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Column(modifier = modifier.padding(16.dp)) {
        Button(
            onClick = { navController.navigate("calendar") }, // Correct route
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Calendar")
        }
    }
}
