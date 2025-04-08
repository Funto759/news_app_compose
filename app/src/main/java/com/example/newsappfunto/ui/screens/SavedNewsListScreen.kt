package com.example.newsappfunto.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.newsappfunto.R
import com.example.newsappfunto.model.NewsViewModel
import com.example.newsappfunto.model.NewsViewModel.NewsViewState
import com.example.newsappfunto.ui.viewsUtil.ArticleListRecyclerScreen
import com.example.newsappfunto.ui.viewsUtil.FragmentTitleCard
import com.example.newsappfunto.ui.viewsUtil.SearchField
import com.example.newsappfunto.ui.viewsUtil.TitleRowWithMenu
import com.example.newsappfunto.ui.viewsUtil.TitleRowWithMenuSave

@Composable
fun SavedNewsListScreen(
    navController: NavController,
    modifier: Modifier
) {
    val context = LocalContext.current
    val viewModel: NewsViewModel = hiltViewModel()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember {
        mutableStateOf(
            context.getSharedPreferences("MyPref2", Context.MODE_PRIVATE)
                .getString("selected_category2", "business") ?: "business"
        )
    }


    LaunchedEffect(Pair(selectedCategory, searchQuery)) {
        if (searchQuery.isNotBlank()) {
            viewModel.getSearchArticles(searchQuery)
        } else if (selectedCategory == "all") {
            viewModel.getArticles()
        } else {
            viewModel.getArticlesCategory(selectedCategory)
        }
    }

    val characters by viewModel.NewsArticleState.collectAsStateWithLifecycle()

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {

        when (characters) {
            is NewsViewModel.NewsViewState.Loading -> {
                val loading = (characters as NewsViewState.Loading).isLoading
                if (loading) {
                    CircularProgressIndicator()
                }
            }

            is NewsViewState.Error -> {
                val error = (characters as NewsViewState.Error).message
                Text(error)

            }

            is NewsViewState.Success -> {
                val articlesList = (characters as NewsViewState.Success).articles
                println(articlesList)
                Column {
                    Spacer(Modifier.height(20.dp))
                    Image(
                        colorFilter = ColorFilter.tint(Color(0xFF1E88E5)),
                        imageVector = Icons.Default.Newspaper,
                        contentDescription = "NYC Logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .height(65.dp)
                            .width(65.dp)
                    )
                    SearchField(
                        searchQuery = searchQuery,
                        onQueryChanged = { newQuery ->
                            searchQuery = newQuery
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(5.dp))
                    TitleRowWithMenuSave(
                        display = selectedCategory,
                        onClick = { selectedCategory = it })
                    if (articlesList.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Replace R.drawable.empty_news with your actual empty state image resource.
                                Image(
                                    painter = painterResource(id = R.drawable.open_carton_box_svgrepo_com),
                                    contentDescription = "No articles",
                                    modifier = Modifier.size(150.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No articles found under \"${selectedCategory.uppercase()}\" category.",
                                    textAlign = TextAlign.Center,
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp), // Adds spacing between items
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp) // Padding for the whole list
                        ) {
                            items(articlesList) { article ->
                                ArticleListRecyclerScreen(
                                    selectedCategory,
                                    article,
                                    Modifier,
                                    navController
                                )
                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }


}





