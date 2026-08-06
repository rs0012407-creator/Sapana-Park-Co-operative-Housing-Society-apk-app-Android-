package com.example.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGold
import com.example.ui.theme.Navy900

sealed class NavItem(val route: String, val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
    object Home : NavItem("home", "Home", Icons.Default.Home, Icons.Outlined.Home)
    object Meetings : NavItem("meetings", "Meetings", Icons.Default.Groups, Icons.Outlined.Group)
    object Complaints : NavItem("complaints", "Complaints", Icons.Default.Build, Icons.Outlined.Build)
    object Community : NavItem("community", "Community", Icons.Default.Group, Icons.Outlined.Group)
    object Documents : NavItem("documents", "Docs", Icons.Default.Description, Icons.Outlined.Description)
    object Profile : NavItem("profile", "Profile", Icons.Default.Person, Icons.Outlined.Person)
    object Settings : NavItem("settings", "Settings", Icons.Default.Settings, Icons.Outlined.Settings)
}

val navItems = listOf(
    NavItem.Home,
    NavItem.Meetings,
    NavItem.Complaints,
    NavItem.Community,
    NavItem.Documents,
    NavItem.Profile
)

@Composable
fun AppBottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        modifier = Modifier.testTag("app_bottom_nav_bar")
    ) {
        navItems.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }
    }
}

@Composable
fun AppNavigationRail(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationRail(
        containerColor = Navy900,
        contentColor = Color.White,
        header = {
            Spacer(modifier = Modifier.height(12.dp))
            Icon(
                imageVector = Icons.Default.Apartment,
                contentDescription = "Sapana Park CHS",
                tint = AmberGold,
                modifier = Modifier.size(32.dp).padding(4.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
        },
        modifier = Modifier.testTag("app_navigation_rail")
    ) {
        navItems.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationRailItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = Color.White
                    )
                }
            )
        }
    }
}

