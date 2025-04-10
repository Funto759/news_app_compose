package com.example.newsappfunto.ui.viewsUtil

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.newsappfunto.R

@Composable
fun EmptyItemView(selectedCategory: String,modifier: Modifier){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val textTitle = if (selectedCategory == "all") {
            "No Saved articles found"
        } else {
            "No articles found under \"${selectedCategory.uppercase()}\" category."
        }

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
                text = textTitle,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}