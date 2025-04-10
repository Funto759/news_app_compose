package com.example.newsappfunto.navigation

import androidx.compose.material.ScaffoldState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.newsappfunto.ui.screens.ArticleDetailScreen.ArticleDetailsWebScreen
import com.example.newsappfunto.ui.screens.ArticleScreen.NewsListScreen
import com.example.newsappfunto.ui.screens.SavedArticleScreen.SavedNewsListScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation(navHostController: NavHostController,scaffoldState: SnackbarHostState){
    NavHost(navController = navHostController, startDestination = "CharactersScreen"){


        composable("CharactersScreen"){
            NewsListScreen(navController = navHostController,scaffoldState)
        }


        composable("BookmarkScreen"){
            SavedNewsListScreen(navHostController,Modifier,scaffoldState)
        }


        composable<NewsDetails>{
            val args =it.toRoute<NewsDetails>()
            ArticleDetailsWebScreen(navHostController, url = args.url, author = args.author.toString(), content = args.content.toString(), description = args.description.toString(), publishedAt = args.publishedAt.toString(), title = args.title.toString(), urlToImage = args.urlToImage.toString(), category = args.category)
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