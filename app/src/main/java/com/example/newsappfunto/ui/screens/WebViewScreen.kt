import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.androiddevs.mvvmnewsapp.models.Source
import com.example.newsappfunto.data.Articles
import com.example.newsappfunto.model.NewsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()
    val viewModel: NewsViewModel = hiltViewModel()
    val db by viewModel.NewsArticleState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.SingleGetArticles(url)
    }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    // Keep a reference to the WebView so we can call methods on it
    var webViewRef: WebView? = null

    val saveState = remember {
        mutableStateOf(false)
    }
    val articleState = remember {
        mutableStateOf<Articles?>(null)
    }
    when(db){
        is NewsViewModel.NewsViewState.SuccessSingle -> {
            val article = (db as NewsViewModel.NewsViewState.SuccessSingle).articles
            articleState.value = article
            saveState.value = !article.url.isNullOrEmpty()
//            saveState.value = article.url == url
        }
        else -> {
            articleState.value = null
        }
    }

    Box {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.mediaPlaybackRequiresUserGesture = true
                    settings.setSupportZoom(true)

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(
                            view: WebView?,
                            url: String?,
                            favicon: Bitmap?
                        ) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                            hasError = false
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                            hasError = false
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)
                            isLoading = false
                            hasError = true
                            errorMessage = error?.description?.toString() ?: "Unknown error"
                        }
                    }

                    loadUrl(url)
                }
            },
            update = { webView ->
                // If URL changes, reload
                if (webView.url != url) {
                    webView.loadUrl(url)
                }
            },
            modifier = Modifier.fillMaxSize()
        )


        // Show progress indicator while loading
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).padding(10.dp))
            }
        }


        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                if (!saveState.value){
                    delay(2000)
                viewModel.saveArticles(Articles(
                    url = url, author = author.toString(), content = content.toString(), description = description.toString(), publishedAt = publishedAt.toString(), title = title.toString(), urlToImage = urlToImage.toString()))
                saveState.value = true

            }else{
                viewModel.SingleGetArticles(url)
                    delay(2000)
                viewModel.deleteArticles(articleState.value!!)
                saveState.value = false
            }}},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp),
        ) {
            if (!saveState.value) {
                Icon(
                    imageVector = Icons.Filled.CloudOff,
                    contentDescription = "Refresh",
                    tint = Color.White
                )
            }else{
                Icon(
                    imageVector = Icons.Outlined.CloudDone,
                    contentDescription = "Refresh",
                    tint = Color.Green
                )
            }
        }
    }
}