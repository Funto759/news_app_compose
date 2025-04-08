package com.example.newsappfunto.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.newsappfunto.R
import com.example.newsappfunto.data.Articles
import com.example.newsappfunto.model.NewsViewModel
import com.example.newsappfunto.model.NewsViewModel.NewsViewState
import com.example.newsappfunto.navigation.NewsDetails

@Composable
fun SavedNewsListScreen(
        navController: NavController,
        modifier: Modifier
    ) {
    val viewModel: NewsViewModel = hiltViewModel()
    var searchQuery by remember { mutableStateOf("") }
    val characters by viewModel.NewsArticleState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.getArticles()
    }

    if (searchQuery.isNotBlank()) {
        LaunchedEffect(searchQuery) {
            viewModel.getSearchArticles(searchQuery)
        }
    } else {
        // Optionally, reload articles when search is cleared.
        LaunchedEffect("clear") {
            viewModel.getArticles()
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(Modifier.height(20.dp))
            Image(
                colorFilter = ColorFilter.tint(Color.Green),
                imageVector = Icons.Default.Newspaper,
                contentDescription = "NYC Logo",
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).height(65.dp).width(65.dp)
            )
            SearchField(
                searchQuery = searchQuery,
                onQueryChanged = { newQuery ->
                    searchQuery = newQuery
                },
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(5.dp))
            InstructionTextDetails("Saved News")
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
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp), // Adds spacing between items
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp) // Padding for the whole list
                    ) {
                        items(articlesList) { article ->
                            CharacterCard(article,navController)
                        }
                    }
                }

                else -> {}
            }
        }


    }

}




