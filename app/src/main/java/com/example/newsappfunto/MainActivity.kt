package com.example.newsappfunto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.newsappfunto.data.BottomNavItem
import com.example.newsappfunto.navigation.Navigation
import com.example.newsappfunto.ui.BottomNavigationBar
import com.example.newsappfunto.ui.theme.NewsAppFuntoTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val bottomNavItem =   listOf(
        BottomNavItem(
            name = "Home",
            route ="CharactersScreen" ,
            image = Icons.Default.Home,
            badgeCount = 0
        ),
        BottomNavItem(
            name = "Bookmarks",
            route ="BookmarkScreen" ,
            image = Icons.Default.Bookmark,
            badgeCount = 105
        ),
        BottomNavItem(
            name = "Settings",
            route ="SettingsScreen" ,
            image = Icons.Default.Settings,
            badgeCount = 0
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppFuntoTheme {
                val navController = rememberNavController()
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