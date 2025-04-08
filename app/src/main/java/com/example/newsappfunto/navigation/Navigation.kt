package com.example.newsappfunto.navigation

import WebViewScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.androiddevs.mvvmnewsapp.models.Source
import com.example.newsappfunto.ui.screens.NewsListScreen
import com.example.newsappfunto.ui.screens.SavedNewsListScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation(navHostController: NavHostController){
    NavHost(navController = navHostController, startDestination = "CharactersScreen"){
        composable("CharactersScreen"){
            NewsListScreen(navController = navHostController)
        }
        composable("BookmarkScreen"){
            SavedNewsListScreen(navHostController,Modifier)
        }
        composable<NewsDetails>{
            val args =it.toRoute<NewsDetails>()
            WebViewScreen(navHostController, url = args.url, author = args.author.toString(), content = args.content.toString(), description = args.description.toString(), publishedAt = args.publishedAt.toString(), title = args.title.toString(), urlToImage = args.urlToImage.toString(), category = args.category)
        }
    }
}

@Serializable
data class NewsDetails (
    val url : String,
        val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val title: String?,
    val urlToImage: String?,
    val category: String?

        )