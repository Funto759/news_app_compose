package com.example.newsappfunto.ui.screens.SavedArticleScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newsappfunto.data.Articles
import com.example.newsappfunto.ui.viewsUtil.ArticleListRecyclerScreen
import com.example.newsappfunto.ui.viewsUtil.EmptyItemView
import com.example.newsappfunto.ui.viewsUtil.SearchField

@Composable
fun SavedArticlesRecyclerView(selectedCategory: String, searchQuery: String, articlesList: List<Articles>, navController: NavController, searchCall:(String) -> Unit, categoryCall:(String) -> Unit) {

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
            initialQuery = searchQuery,
            onQueryChanged = { newQuery ->
                searchCall(newQuery)
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(5.dp))
        TitleRowWithMenuSave(
            search = {searchCall(it)},
            display = selectedCategory,
            onClick = { categoryCall(it)})
        if (articlesList.isEmpty()) {
            EmptyItemView(selectedCategory, Modifier)
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