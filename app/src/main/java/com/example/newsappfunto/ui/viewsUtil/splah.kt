package com.example.newsappfunto.ui.viewsUtil

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.newsappfunto.R

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onAnimationFinished: () -> Unit = {}
) {
    // Box is used to center the animated image
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.news_svgrepo_com),
            contentDescription = "Animated Newspaper Splash",
            modifier = Modifier.size(100.dp)
        )
    }
}
