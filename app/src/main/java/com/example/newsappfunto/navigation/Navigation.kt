package com.example.newsappfunto.navigation

import android.annotation.SuppressLint
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.compose_notes.ui.screens.logIn.LoginScreen
import com.example.compose_notes.ui.screens.signUp.SignUpScreen
import com.example.newsappfunto.ui.screens.ArticleDetailScreen.ArticleDetailsWebScreen
import com.example.newsappfunto.ui.screens.ArticleScreen.NewsListScreen
import com.example.newsappfunto.ui.screens.SavedArticleScreen.SavedNewsListScreen
import com.example.newsappfunto.ui.screens.profile.ProfileScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation(navHostController: NavHostController,scaffoldState: SnackbarHostState,status: Boolean){
    var start = if (status) "CharactersScreen" else "LogInScreen"
    NavHost(navController = navHostController, startDestination = start){


        composable("CharactersScreen"){
            NewsListScreen(navController = navHostController,scaffoldState)
        }
        composable("SignUpScreen"){
            SignUpScreen(navController = navHostController,scaffoldState)
        }
        composable("LogInScreen"){
            LoginScreen(navController = navHostController,scaffoldState)
        }
        composable("ProfileScreen"){
           ProfileScreen(navController = navHostController,scaffoldState)
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

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class NewsDetails (
    val url : String,
        val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val title: String?,
    val urlToImage: String?,
    val category: String?)