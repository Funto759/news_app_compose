package com.example.newsappfunto.navigation.bottomNav

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.BadgedBox
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.newsappfunto.data.BottomNavItem

/**
 * Composable function that creates a bottom navigation bar.
 *
 * @param items A list of [BottomNavItem] objects representing the navigation items to display.
 * @param navController The [NavController] used to manage navigation within the application.
 * @param modifier Optional [Modifier] to apply to the bottom navigation bar.  Defaults to [Modifier].
 * @param onItemClick Lambda function to be executed when a navigation item is clicked.  It receives the clicked [BottomNavItem] as a parameter.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    items : List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
){
val backStackEntry = navController.currentBackStackEntryAsState()

    BottomNavigation(modifier = modifier, backgroundColor = androidx.compose.ui.graphics.Color.DarkGray, elevation = 5.dp) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = Color.White, // This will be applied to the Icon
                unselectedContentColor = Color.Gray, // This will be applied to the Icon
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        BadgedBox(
                            badge = {
                                if (item.badgeCount > 0) {
                                    Badge(
                                        containerColor = Color.Red,
                                        contentColor = Color.White
                                    ) {
                                        Text("${item.badgeCount}")
                                    }
                                }
                            }
                        ) {
                            if (selected) {
                                Icon(
                                    imageVector = item.selectedImage,
                                    contentDescription = "NavIcon"
                                )
                            } else {
                                Icon(
                                    imageVector = item.unSelectedImage,
                                    contentDescription = "NavIcon"
                                )
                            }
                        }
                        Spacer(Modifier.height(1.dp))
                        if (selected) {
                            Text(
                                item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp,
                                color = Color(0xFF1E88E5)// Apply color to selected text here
                            )
                        } else {

                            Text(
                                item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp,
                                color = Color(0xFF4D5D6C) // Apply color to unselected text here
                            )
                        }
                    }
                }
            )

        }
    }
}