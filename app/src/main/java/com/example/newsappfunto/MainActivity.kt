package com.example.newsappfunto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsappfunto.data.Articles
import com.example.newsappfunto.data.BottomNavItem
import com.example.newsappfunto.model.FirebaseAuthentificationViewModel
import com.example.newsappfunto.model.FirebaseAuthentificationViewModel.FirebaseViewState
import com.example.newsappfunto.model.NewsViewModel
import com.example.newsappfunto.navigation.Navigation
import com.example.newsappfunto.navigation.bottomNav.BottomNavigationBar
import com.example.newsappfunto.ui.theme.NewsAppFuntoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppFuntoTheme {
                val navController = rememberNavController()
                val viewModel:NewsViewModel = hiltViewModel()

                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val articleState = remember { mutableStateOf<List<Articles>>(emptyList()) }
                val navigationStatus = remember { mutableStateOf(true) }
                val signUpVewModel: FirebaseAuthentificationViewModel = hiltViewModel()
                val newsArticles by signUpVewModel.articlesFlow.collectAsStateWithLifecycle()


                val status by signUpVewModel.signUpStatus.collectAsStateWithLifecycle()

LaunchedEffect(Unit) {
    signUpVewModel.checkAuthStatus()
}

                LaunchedEffect(currentBackStackEntry) {
                    currentBackStackEntry?.lifecycle?.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                       signUpVewModel.retrieveArticlesListener()
                    }
                }
                when(status){
                    is FirebaseViewState.Authenticated -> {
                       navigationStatus.value = true
                        println("authenticated")
                    }
                    is FirebaseViewState.UnAuthenticated -> {
                        navigationStatus.value = false
                        println("unauthenticated")
                    }
                    is FirebaseViewState.Loading -> {
                        println("loading")
                    }

                    else -> {}
                }

                when(newsArticles){
                    is FirebaseViewState.Success -> {
                        val article = (newsArticles as FirebaseViewState.Success).articles
                        if (article.isNotEmpty()) {
                            articleState.value = article
                            println("News Articles: ${articleState.value.size}")
                        } else{
                            articleState.value = emptyList()
                        }
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
                    ),
                            BottomNavItem(
                        name = "Profile",
                        route ="ProfileScreen" ,
                        selectedImage = Icons.Filled.PersonSearch,
                        unSelectedImage = Icons.Outlined.PersonSearch,
                        badgeCount = 0
                    )
                )
                val scaffoldState = remember { SnackbarHostState() }

                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {if (navigationStatus.value) BottomNavigationBar(modifier = Modifier.navigationBarsPadding(),items = bottomNavItem
                        , navController = navController, onItemClick = { navController.navigate(it.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                        }
                        })
                    else null},
                    snackbarHost = {SnackbarHost(hostState = scaffoldState)}
                )
                { innerPadding ->

                    Surface (modifier = Modifier.padding(innerPadding).navigationBarsPadding()){
                        if (navigationStatus.value){
                            Navigation(navController,scaffoldState,true)
                        }else{
                            Navigation(navController,scaffoldState,false)
                        }
                    }
                }
            }
        }
    }
}
