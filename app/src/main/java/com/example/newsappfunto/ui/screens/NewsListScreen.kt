@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.newsappfunto.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRowDefaults.Indicator
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.AlignmentLine
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
//    val state by viewModel.screenState.collectAsStateWithLifecycle()
//    LaunchedEffect(viewModel) {
//        viewModel.getNewsArticles()
//    }

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
//                    val loading = (characters as NewsViewState.Loading).isLoading
//                    if (loading) {
                        Spacer(Modifier.height(5.dp))
                        InstructionTextDetails("Breaking News")
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center).padding(10.dp)
                            )
                        }
                }


                is LoadState.Error -> {
                        Spacer(Modifier.height(5.dp))
                        InstructionTextDetails("Breaking News")
//                        val error = (characters as NewsViewState.Error).message
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "error",
                                modifier = Modifier.align(Alignment.Center).padding(10.dp)
                            )
                        }
                }

                else -> {
//                    val articlesList = (characters as NewsViewState.Success).articles
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
fun SearchField(
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.TextField(
        value = searchQuery,
        onValueChange = onQueryChanged,
        placeholder = { Text(text = "Search") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search icon"
            )
        },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}



@Composable
fun InstructionTextDetails(display: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD) // Light background for contrast
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "📚 $display",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color(0xFF1E88E5), // Soft blue for the text
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
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
            InstructionTextDetails("Breaking News")
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
                        CharacterCard(char, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterCard(it: Articles,navController: NavController){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(NewsDetails(it.url.toString(),it.author.toString(),it.content.toString(),it.description.toString(),it.publishedAt.toString(),it.title.toString(),it.urlToImage.toString())) }
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),

        ) {
        Column (modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally){
            // Book Rank

            Text(
                text = it.title.toString(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = Color(0xFF47EE00),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Book Image
            Image(
                painter = rememberAsyncImagePainter(it.urlToImage),
                contentDescription = "Book Cover",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Title and Author
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = it.description.toString(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it.publishedAt.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it.content.toString(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}