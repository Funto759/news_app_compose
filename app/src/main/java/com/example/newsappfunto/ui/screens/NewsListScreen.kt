@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsappfunto.data.Articles
import com.example.newsappfunto.model.NewsViewModel
import com.example.newsappfunto.ui.theme.NewsAppFuntoTheme
import com.example.newsappfunto.ui.viewsUtil.ArticleListRecyclerScreen
import com.example.newsappfunto.ui.viewsUtil.FragmentTitleCard
import com.example.newsappfunto.ui.viewsUtil.SearchField
import com.example.newsappfunto.ui.viewsUtil.TitleRowWithMenu


@Composable
fun NewsListScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val viewModel: NewsViewModel = hiltViewModel()

    var selectedCategory by remember {
        mutableStateOf(
            context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
                .getString("selected_category", "business") ?: "business"
        )
    }
    // Other state variables…
    var searchQuery by remember { mutableStateOf("") }
    val newsFlow = remember(selectedCategory, searchQuery) {
        if (searchQuery.isNotBlank()) {
            viewModel.getSearchNews(searchQuery)
        } else {
            viewModel.getNews(selectedCategory)
        }
    }
    val characters = newsFlow.collectAsLazyPagingItems()


    val isRefreshing = characters.loadState.refresh is androidx.paging.LoadState.Loading
    val isError = characters.loadState.refresh is androidx.paging.LoadState.Error


    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(Modifier.height(20.dp))
            Image(
                colorFilter = ColorFilter.tint(Color(0xFF4D5D6C)),
                imageVector = Icons.Default.Newspaper,
                contentDescription = "NYC Logo",
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).height(65.dp)
                    .width(65.dp)
            )
            SearchField(
                searchQuery = searchQuery,
                onQueryChanged = { newQuery ->
                    searchQuery = newQuery
                },
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            )
            when (characters.loadState.refresh) {
                is LoadState.Loading -> {
                        Spacer(Modifier.height(5.dp))
                    TitleRowWithMenu(selectedCategory.uppercase(), onClick = {})
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center).padding(10.dp)
                            )
                        }
                }


                is LoadState.Error -> {
                        Spacer(Modifier.height(5.dp))
                    TitleRowWithMenu(selectedCategory.uppercase(), onClick = {})
//                        val error = (characters as NewsViewState.Error).message
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "error",
                                modifier = Modifier.align(Alignment.Center).padding(10.dp)
                            )
                        }
                }

                else -> {
                    println(characters)
                    PullToRefreshCustomStyleSample(selectedCategory = selectedCategory, viewModel,
                        navController, characters, isRefreshing = isRefreshing,
                        onRefresh = { characters.refresh() },
                        onCLick = {selectedCategory = it},
                        modifier = Modifier
                    )
                }

            }
        }
    }

}




/**
 * A composable function that displays a list of articles with pull-to-refresh functionality and a custom styling.
 *
 * @param navController The navigation controller for navigating between screens.
 * @param items The lazy paging items containing the list of articles.
 * @param isRefreshing A boolean indicating whether the data is currently being refreshed.
 * @param onRefresh A callback function to be executed when a refresh is triggered.
 * @param modifier The modifier to be applied to the composable. Defaults to [Modifier].
 */
@Composable
fun PullToRefreshCustomStyleSample(
    selectedCategory: String,
    viewModel: NewsViewModel,
    navController: NavController,
    items: LazyPagingItems<Articles>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onCLick:(String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        state = state
    ) {
        Column {
            Spacer(Modifier.height(5.dp))
            TitleRowWithMenu(selectedCategory.uppercase(), onClick = {
                onCLick(it)
            })
            Spacer(Modifier.height(5.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp), // Adds spacing between items
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp) // Padding for the whole list
            ) {
                items(count = items.itemCount) {
                    val char = items[it]
                    if (char != null) {
                        ArticleListRecyclerScreen(selectedCategory,char, Modifier,navController)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewsAppFuntoTheme {
        // Create a dummy NavController instance for preview.
        val navController = rememberNavController()
        NewsListScreen(navController = navController)
    }
}

