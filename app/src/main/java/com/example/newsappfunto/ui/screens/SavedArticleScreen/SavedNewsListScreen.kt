package com.example.newsappfunto.ui.screens.SavedArticleScreen

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.newsappfunto.model.FirebaseAuthentificationViewModel
import com.example.newsappfunto.model.FirebaseAuthentificationViewModel.FirebaseViewState
import com.example.newsappfunto.model.NewsViewModel
import com.example.newsappfunto.model.NewsViewModel.NewsViewState

@Composable
fun SavedNewsListScreen(
    navController: NavController,
    modifier: Modifier,
    scaffoldState: SnackbarHostState
) {
    val context = LocalContext.current
    val viewModel: NewsViewModel = hiltViewModel()
    val signUpViewModel: FirebaseAuthentificationViewModel = hiltViewModel()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember {
        mutableStateOf(
            context.getSharedPreferences("MyPref2", Context.MODE_PRIVATE)
                .getString("selected_category2", "all") ?: "business"
        )
    }
//    LaunchedEffect(Unit) {
//        signUpViewModel.retrieveArticlesListener(selectedCategory)
//    }
//    LaunchedEffect(selectedCategory, searchQuery) {
//        if (searchQuery.isNotBlank()) {
//            viewModel.getSearchArticles(searchQuery)
//        } else if (selectedCategory == "all") {
//            viewModel.getArticles()
//        } else {
//            viewModel.getArticlesCategory(selectedCategory)
//        }
//    }
    LaunchedEffect(selectedCategory, searchQuery) {
        if (searchQuery.isNotBlank()) {
            signUpViewModel.retrieveArticlesSearchListener(searchQuery)
        } else if (selectedCategory == "all") {
            signUpViewModel.retrieveArticlesListener()
        } else {
            signUpViewModel.retrieveArticlesCategoryListener(selectedCategory)
        }
    }
//    LaunchedEffect(selectedCategory) {
//        scaffoldState.showSnackbar(message = "${selectedCategory.uppercase()} Category selected",duration = SnackbarDuration.Short)
//    }

//    val characters by viewModel.NewsArticleState.collectAsStateWithLifecycle()
    val characters by signUpViewModel.articlesFlow.collectAsStateWithLifecycle()

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        when (characters) {
            is FirebaseViewState.Loading -> {
                val loading = (characters as FirebaseViewState.Loading).loading
                if (loading) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center).padding(10.dp)
                        )
                    }
                }
            }

            is FirebaseViewState.Error -> {
                val error = (characters as FirebaseViewState.Error).message
                Text(error)

            }

            is FirebaseViewState.Success -> {
                val articlesList = (characters as FirebaseViewState.Success).articles
                println(articlesList)
             SavedArticlesRecyclerView(selectedCategory, searchQuery, articlesList, navController, searchCall = {searchQuery = it}, categoryCall = {selectedCategory = it})
            }

            else -> {}
        }
    }


}





