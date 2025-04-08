@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.newsappfunto.ui.screens

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsappfunto.data.Articles
import com.example.newsappfunto.model.NewsViewModel
import com.example.newsappfunto.ui.viewsUtil.ArticleListRecyclerScreen
import com.example.newsappfunto.ui.viewsUtil.FragmentTitleCard
import com.example.newsappfunto.ui.viewsUtil.SearchField


@Composable
fun NewsListScreen(
    navController: NavController,
) {

    val viewModel: NewsViewModel = hiltViewModel()
    var searchQuery by remember { mutableStateOf("") }
    val newsFlow = remember(searchQuery) {
        if (searchQuery.isNotBlank()) {
            viewModel.getSearchNews(searchQuery)
        } else {
            viewModel.getNews()
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
            Spacer(Modifier.height(40.dp))
            Image(
                colorFilter = ColorFilter.tint(Color.Green),
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
                        FragmentTitleCard("Breaking News")
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center).padding(10.dp)
                            )
                        }
                }


                is LoadState.Error -> {
                        Spacer(Modifier.height(5.dp))
                        FragmentTitleCard("Breaking News")
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
                    PullToRefreshCustomStyleSample(
                        navController, characters, isRefreshing = isRefreshing,
                        onRefresh = { characters.refresh() },
                        modifier = Modifier
                    )
                }

            }
        }
    }

}




@Composable
fun PullToRefreshCustomStyleSample(
    navController: NavController,
    items: LazyPagingItems<Articles>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
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
            FragmentTitleCard("Breaking News")
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
                        ArticleListRecyclerScreen(char, navController)
                    }
                }
            }
        }
    }
}

