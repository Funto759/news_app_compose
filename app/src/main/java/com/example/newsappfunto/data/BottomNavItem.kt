package com.example.newsappfunto.data

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val name :String,
    val route : String,
    val image: ImageVector,
    val badgeCount : Int = 0
)
