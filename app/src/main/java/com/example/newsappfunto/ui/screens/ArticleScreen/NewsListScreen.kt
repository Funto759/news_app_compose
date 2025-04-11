@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.newsappfunto.ui.screens.ArticleScreen

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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsappfunto.R
import com.example.newsappfunto.data.Articles
import com.example.newsappfunto.model.NewsViewModel
import com.example.newsappfunto.ui.viewsUtil.ArticleListRecyclerScreen
import com.example.newsappfunto.ui.viewsUtil.EmptyItemView
import com.example.newsappfunto.ui.viewsUtil.SearchField


@Composable
fun NewsListScreen(
    navController: NavController,
    scaffoldState: SnackbarHostState
) {
    val context = LocalContext.current
    val viewModel: NewsViewModel = hiltViewModel()
    var selectedCategory by remember { mutableStateOf(
            context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
                .getString("selected_category", "business") ?: "business"
        )
    }
    var searchQuery by remember { mutableStateOf("") }

    val newsFlow = remember(selectedCategory, searchQuery) {
        if (searchQuery.isNotBlank()) {
            viewModel.getSearchNews(searchQuery)
        } else {
            viewModel.getNews(selectedCategory)
        }
    }
    val characters = newsFlow.collectAsLazyPagingItems()


    LaunchedEffect(selectedCategory) {
        scaffoldState.showSnackbar(message = "${selectedCategory.uppercase()} Category selected",duration = SnackbarDuration.Short)
    }


    val isRefreshing = characters.loadState.refresh is LoadState.Loading
    val isError = characters.loadState.refresh is LoadState.Error


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
                initialQuery = searchQuery,
                onQueryChanged = { newQuery ->
                    searchQuery = newQuery
                },
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            )
            when (characters.loadState.refresh) {
                is LoadState.Loading -> {
                        Spacer(Modifier.height(5.dp))
                    TitleRowWithMenu(
                        search = {searchQuery =it},
                        selectedCategory.uppercase(), onClick = {})
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center).padding(10.dp)
                            )
                        }
                }

                is LoadState.Error -> {
                        Spacer(Modifier.height(5.dp))
                    TitleRowWithMenu(
                        search = {searchQuery = it},
                        selectedCategory.uppercase(), onClick = {})
                    val errorText = "main"
                      EmptyItemView(errorText,Modifier)
                }

                else -> {
                    println(characters)
                    PullToRefreshCustomStyleSample(
                        search = {searchQuery = it},
                        selectedCategory = selectedCategory, viewModel,
                        navController, characters, isRefreshing = isRefreshing,
                        onRefresh = { characters.refresh() },
                        onCLick = {
                            searchQuery = ""
                            selectedCategory = it},
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
@ExperimentalMaterial3Api
@Composable
fun PullToRefreshCustomStyleSample(
    search:(String) -> Unit,
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
            TitleRowWithMenu(
                search = {search(it)},
                selectedCategory.uppercase(),
            onClick = {
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

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    NewsAppFuntoTheme {
//        // Create a dummy NavController instance for preview.
//        val navController = rememberNavController()
//        NewsListScreen(navController = navController)
//    }
//}

