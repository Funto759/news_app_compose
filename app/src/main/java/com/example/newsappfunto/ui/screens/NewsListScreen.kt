package com.example.newsappfunto.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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

@Composable
fun NewsListScreen(
        navController: NavController,
    ) {
    val viewModel: NewsViewModel = hiltViewModel()

    val characters by viewModel.NewsListState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getNewsArticles()
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(Modifier.height(20.dp))
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "NYC Logo",
                Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(5.dp))
            InstructionTextDetails("Rick And Morty")
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
                    val articles = (characters as NewsViewState.Success).articles
                    Recycler(articles,navController)
                }

                else -> {}
            }
        }


    }

}


        @Composable
        fun searchBar(
            modifier: Modifier = Modifier,
            hint: String = "",
            onSearch: (String) -> Unit = {}
        ) {

            var hintText by remember { mutableStateOf("") }

            var isHintDisplayed by remember { mutableStateOf(hint != "") }

            Box(Modifier) {
                BasicTextField(
                    value = hintText,
                    onValueChange = {
                        hintText = it
                        onSearch(hintText)
                    },
                    maxLines = 1,
                    singleLine = true,
                    textStyle = TextStyle(Color.Black),
                    modifier = Modifier.fillMaxWidth().shadow(5.dp, CircleShape).background(
                        Color.White,
                        CircleShape
                    ).padding(horizontal = 20.dp, vertical = 12.dp)
                )
            }

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
fun Recycler(characters: List<Articles>,navController: NavController) {
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(count = characters.size) {
            val char = characters[it]
            if (char != null) {
                CharacterCard(char,navController)
            }
        }
    }
}

@Composable
fun CharacterCard(it: Articles,navController: NavController){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
//            .clickable { navController.navigate(CharactersDetailScreen(it.url)) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Book Rank
            Text(
                text = "Status #${it.title}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = Color(0xFF6200EE),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Book Image
            Image(
                painter = rememberAsyncImagePainter(it.urlToImage),
                contentDescription = "Book Cover",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Title and Author
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = it.title.toString(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "by ${it.source}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it.publishedAt.toString(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Price Section
            Text(
                text = "${it.source?.name}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = Color(0xFF03DAC5)
            )
        }
    }
}


    @Preview(showBackground = true)
    @Composable
    private fun preview(name: String = "Manga"){
//        NYCTimesTheme {
//            InstructionTextDetails(name)
//        }
    }

