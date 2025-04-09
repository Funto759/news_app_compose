package com.example.newsappfunto.ui.viewsUtil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.newsappfunto.data.Articles
import com.example.newsappfunto.navigation.NewsDetails
import com.example.newsappfunto.util.formatDate


/**
 * Composable function to display a single article item in a list.
 *
 * @param it The [Articles] object containing the article data.
 * @param navController The [NavController] used for navigation to the article details screen.
 */
@Composable
fun ArticleListRecyclerScreen
            (
    selectedCategory: String,
    article: Articles,
    modifier: Modifier = Modifier,
    navController: NavController

) {
    // Each card is clickable to open the article
    Card(
        onClick = { navController.navigate(
            NewsDetails(
                article.url.toString(),
                article.author.toString(),
                article.content.toString(),
                article.description.toString(),
                article.publishedAt.toString(),
                article.title.toString(),
                article.urlToImage.toString(),
                selectedCategory
                )
        )
        },
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // Header image
            article.urlToImage?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = article.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text( color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                text = article.title.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Description (if present)
            article.description?.let { desc ->
                Spacer(modifier = Modifier.height(4.dp))
                Text( color = Color(0xFF4D5D6C),
                    text = desc,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            // Author and publication date
            Text(
                text = buildString {
                    if (!article.author.isNullOrBlank()) append("By ${article.author} • ")
                    if(!article.publishedAt.isNullOrBlank()) append(formatDate(article.publishedAt.toString()))
                },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}