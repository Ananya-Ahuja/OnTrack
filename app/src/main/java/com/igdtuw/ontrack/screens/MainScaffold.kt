package com.igdtuw.ontrack.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.igdtuw.ontrack.AuthViewModel
import com.igdtuw.ontrack.R
import kotlinx.coroutines.launch

data class DrawerItem(
    val label: String,
    @DrawableRes val iconRes: Int
)

val drawerItems = listOf(
    DrawerItem("Home", R.drawable.home),
    DrawerItem("To-do List", R.drawable.Todo),
    DrawerItem("Calendar", R.drawable.calendar),
    DrawerItem("ProjectPlanner", R.drawable.project),
    DrawerItem("CodeVault", R.drawable.codevault),
    DrawerItem("Resources", R.drawable.resources),
    DrawerItem("CGPA Calculator", R.drawable.cgpa)
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    screenTitle: String,
    canNavigateBack: Boolean,
    onNavigateBack: () -> Unit,
    onDrawerItemClick: (String) -> Unit,
    onLogoutClick: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val userName by authViewModel.userName.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                userName = userName,
                onItemClick = {
                    onDrawerItemClick(it)
                    scope.launch { drawerState.close() }
                },
                onLogoutClick = {
                    onLogoutClick()
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                CenteredTopBar(
                    title = screenTitle,
                    canNavigateBack = canNavigateBack,
                    onNavigateBack = onNavigateBack,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredTopBar(
    title: String,
    canNavigateBack: Boolean,
    onNavigateBack: () -> Unit,
    onMenuClick: () -> Unit
) {
    TopAppBar(
        title = {
            Box(Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun AppDrawer(
    userName: String,
    onItemClick: (String) -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Color.White)
    ) {
        // Drawer header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFA152E6)) // Purple
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color(0xFFA152E6),
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = userName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Drawer items
        drawerItems.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item.label) }
                    .padding(vertical = 12.dp, horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = item.iconRes),
                    contentDescription = item.label,
                    colorFilter = ColorFilter.tint(Color(0xFFA152E6)),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = item.label,
                    color = Color(0xFF2D0A37),
                    fontSize = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Divider(modifier = Modifier.padding(horizontal = 16.dp))
        // Logout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onLogoutClick() }
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logout), // Your logout icon
                contentDescription = "Logout",
                colorFilter = ColorFilter.tint(Color.Red), // Keep the red tint
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Logout",
                color = Color.Red,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}
