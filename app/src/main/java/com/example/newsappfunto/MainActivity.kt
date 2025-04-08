package com.example.newsappfunto

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsappfunto.data.Articles
import com.example.newsappfunto.data.BottomNavItem
import com.example.newsappfunto.model.NewsViewModel
import com.example.newsappfunto.model.SplashViewModel
import com.example.newsappfunto.navigation.Navigation
import com.example.newsappfunto.ui.BottomNavigationBar
import com.example.newsappfunto.ui.theme.NewsAppFuntoTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
//        val splashScreen = installSplashScreen().apply {
//            setOnExitAnimationListener { viewProvider ->
//                ObjectAnimator.ofFloat(
//                    viewProvider.view,
//                    "scaleX",
//                    0.5f, 0f
//                ).apply {
//                    interpolator = OvershootInterpolator()
//                    duration = 300
//                    doOnEnd { viewProvider.remove() }
//                    start()
//                }
//                ObjectAnimator.ofFloat(
//                    viewProvider.view,
//                    "scaleY",
//                    0.5f, 0f
//                ).apply {
//                    interpolator = OvershootInterpolator()
//                    duration = 300
//                    doOnEnd { viewProvider.remove() }
//                    start()
//                }
//            }
//        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        splashScreen.setKeepOnScreenCondition {
//            splashViewModel.isSplashShow.value
//        }
        val splashscreen = installSplashScreen()
//        var keepSplashScreen = true
        splashscreen.setKeepOnScreenCondition { splashViewModel.isSplashShow.value }
//        lifecycleScope.launch {
//            delay(5000)
//            keepSplashScreen = false
//        }
        setContent {
            NewsAppFuntoTheme {
                val navController = rememberNavController()
                val viewModel:NewsViewModel = hiltViewModel()
                val db by viewModel.NewsArticleState.collectAsStateWithLifecycle()

                val currentBackStackEntry by navController.currentBackStackEntryAsState()

                // Use LaunchedEffect keyed on the currentBackStackEntry
                LaunchedEffect(currentBackStackEntry) {
                    currentBackStackEntry?.lifecycle?.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        viewModel.getArticles()
                    }
                }
//                LaunchedEffect (viewModel){
//                    viewModel.getArticles()
//                }
                val articleState = remember { mutableStateOf<List<Articles>>(emptyList()) }

                when(db){
                    is NewsViewModel.NewsViewState.Success -> {
                        val article = (db as NewsViewModel.NewsViewState.Success).articles
                        articleState.value = article
                        println("Sizesss ${articleState.value.size}")
                    }
                    else -> {
                        articleState.value = emptyList()
                    }
                }
                val bottomNavItem =   listOf(
                    BottomNavItem(
                        name = "Home",
                        route ="CharactersScreen" ,
                        selectedImage = Icons.Filled.Home,
                        unSelectedImage = Icons.Outlined.Home,
                        badgeCount = 0
                    ),
                    BottomNavItem(
                        name = "Saved Articles",
                        route ="BookmarkScreen" ,
                        selectedImage = Icons.Filled.Bookmarks,
                        unSelectedImage = Icons.Outlined.Bookmarks,
                        badgeCount = articleState.value.size
                    )
                )
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {  BottomNavigationBar(modifier = Modifier.navigationBarsPadding(),items = bottomNavItem
                        , navController = navController, onItemClick = { navController.navigate(it.route)})
                    }) { innerPadding ->
                    Surface (modifier = Modifier.padding(innerPadding).navigationBarsPadding()){
                        Navigation(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewsAppFuntoTheme {
        Greeting("Android")
    }
}