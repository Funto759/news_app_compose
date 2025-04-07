package com.example.newsappfunto.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Composable
fun BookmarkScreen(navController: NavController,modifier: Modifier){
    Box(Modifier.fillMaxSize(),
        Alignment.Center){
        Text("Bookmarks",color = Color.White)
    }
}

@Preview
@Composable
fun BookmarkScreenPreview(){
    BookmarkScreen(navController = NavController(LocalContext.current),Modifier)
}