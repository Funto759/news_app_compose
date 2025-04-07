import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.androiddevs.mvvmnewsapp.models.Source
import com.example.newsappfunto.data.Articles
import com.example.newsappfunto.model.NewsViewModel

@Composable
fun WebViewScreen(
    navController: NavController,
    url: String,
     author: String,
    content: String,
    description: String,
     publishedAt: String,
     title: String,
     urlToImage: String
) {

    val viewModel: NewsViewModel = hiltViewModel()
    // Keep a reference to the WebView so we can call methods on it
    var webViewRef: WebView? = null

    Box {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    settings.loadWithOverviewMode = true
                    settings.mediaPlaybackRequiresUserGesture = true
                    settings.setSupportZoom(true)
                    webViewRef = this
                    loadUrl(url)
                }
            },
            update = { webView ->
                // If URL changes, reload
                if (webView.url != url) {
                    webView.loadUrl(url)
                }
                webViewRef = webView
            },
            modifier = Modifier.fillMaxSize()
        )

        FloatingActionButton(
            onClick = { viewModel.saveArticles(Articles(
                url = url, author = author.toString(), content = content.toString(), description = description.toString(), publishedAt = publishedAt.toString(), title = title.toString(), urlToImage = urlToImage.toString()))},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Savings,
                contentDescription = "Refresh"
            )
        }
    }
}
