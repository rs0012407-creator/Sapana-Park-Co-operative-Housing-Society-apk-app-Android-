package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.AppNavigationRail
import com.example.ui.components.EmergencyBottomSheet
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.communication.CommunicationScreen
import com.example.ui.screens.community.CommunityScreen
import com.example.ui.screens.complaints.ComplaintsScreen
import com.example.ui.screens.dashboard.HomeScreen
import com.example.ui.screens.documents.DocumentsScreen
import com.example.ui.screens.meetings.MeetingsScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.Navy900
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.SocietyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContainer()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContainer() {
    val societyViewModel: SocietyViewModel = viewModel()
    val navController = rememberNavController()
    val userSession by societyViewModel.userSession.collectAsState()
    val userMessage by societyViewModel.userMessage.collectAsState()

    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    var showEmergencySheet by remember { mutableStateOf(false) }

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            societyViewModel.clearMessage()
        }
    }

    if (showEmergencySheet) {
        EmergencyBottomSheet(viewModel = societyViewModel, onDismiss = { showEmergencySheet = false })
    }

    val isAuthScreen = currentRoute == "auth" || !userSession.isLoggedIn

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 600.dp

        Row(modifier = Modifier.fillMaxSize()) {
            // Adaptive Navigation Rail for Wide Screens (Tablets, Foldables, Landscape, Desktop)
            if (!isAuthScreen && isWideScreen) {
                AppNavigationRail(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            Scaffold(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                topBar = {
                    if (!isAuthScreen) {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Sapana Park CHS",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = Color.White
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    navController.navigate("home") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Apartment,
                                        contentDescription = "Home",
                                        tint = AmberGold,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { showEmergencySheet = true }) {
                                    Icon(
                                        imageVector = Icons.Default.PhoneInTalk,
                                        contentDescription = "Emergency Hotline",
                                        tint = CrimsonRed
                                    )
                                }
                                IconButton(onClick = { navController.navigate("communication") }) {
                                    Icon(
                                        imageVector = Icons.Default.Group,
                                        contentDescription = "Committee",
                                        tint = Color.White
                                    )
                                }
                                IconButton(onClick = { navController.navigate("settings") }) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Settings",
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Navy900
                            )
                        )
                    }
                },
                bottomBar = {
                    // Show Bottom Navigation Bar only on Compact Screens (< 600dp)
                    if (!isAuthScreen && !isWideScreen) {
                        AppBottomNavBar(
                            currentRoute = currentRoute,
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .widthIn(max = 1200.dp)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = if (userSession.isLoggedIn) "home" else "auth"
                        ) {
                            composable("auth") {
                                AuthScreen(
                                    viewModel = societyViewModel,
                                    onLoginSuccess = {
                                        navController.navigate("home") {
                                            popUpTo("auth") { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable("home") {
                                HomeScreen(
                                    viewModel = societyViewModel,
                                    onNavigateToMeetings = { navController.navigate("meetings") },
                                    onNavigateToComplaints = { navController.navigate("complaints") },
                                    onNavigateToDocuments = { navController.navigate("documents") },
                                    onNavigateToCommunity = { navController.navigate("community") },
                                    onNavigateToCommunication = { navController.navigate("communication") }
                                )
                            }

                            composable("meetings") {
                                MeetingsScreen(viewModel = societyViewModel)
                            }

                            composable("complaints") {
                                ComplaintsScreen(viewModel = societyViewModel)
                            }

                            composable("community") {
                                CommunityScreen(viewModel = societyViewModel)
                            }

                            composable("documents") {
                                DocumentsScreen(viewModel = societyViewModel)
                            }

                            composable("communication") {
                                CommunicationScreen(viewModel = societyViewModel)
                            }

                            composable("profile") {
                                ProfileScreen(viewModel = societyViewModel)
                            }

                            composable("settings") {
                                SettingsScreen(
                                    viewModel = societyViewModel,
                                    onLogout = {
                                        navController.navigate("auth") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

