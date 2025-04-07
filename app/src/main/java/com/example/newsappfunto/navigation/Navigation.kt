package com.example.newsappfunto.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newsappfunto.ui.screens.BookmarkScreen
import com.example.newsappfunto.ui.screens.NewsListScreen
import com.example.newsappfunto.ui.screens.SettingsScreen

@Composable
fun Navigation(navHostController: NavHostController){
    NavHost(navController = navHostController, startDestination = "CharactersScreen"){
        composable("CharactersScreen"){
            NewsListScreen(navController = navHostController)
        }
        composable("SettingsScreen"){
            SettingsScreen(navController = navHostController,Modifier)
        }
        composable("BookmarkScreen"){
            BookmarkScreen(navHostController,Modifier)
        }
    }
}