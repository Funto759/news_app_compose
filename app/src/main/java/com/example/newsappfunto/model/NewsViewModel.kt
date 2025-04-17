package com.example.newsappfunto.model

import android.icu.text.StringSearch
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.newsappfunto.data.Articles
import com.example.newsappfunto.service.NewsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsappfunto.dao.NewsArticlesDatabase
import com.example.newsappfunto.util.ResultWrapper
import com.example.newsappfunto.util.safeApiCall
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(val api: NewsApi,
    val db: NewsArticlesDatabase
): ViewModel(){

    var _NewsListState = MutableStateFlow<NewsViewState>(NewsViewState.IDLE)
    val NewsListState = _NewsListState.asStateFlow()
    var _NewsArticleState = MutableStateFlow<NewsViewState>(NewsViewState.IDLE)
    val NewsArticleState = _NewsArticleState.asStateFlow()

    private var _isRefreshing = MutableStateFlow(false)
    val isRefreshing= _isRefreshing.asStateFlow()


    /**
     * Retrieves a paginated stream of breaking news articles.
     *
     * This function utilizes the Paging 3 library to efficiently load and manage a large set of articles
     * from a remote data source (represented by the `BreakingNewsSource`).  The results are presented
     * as a `Flow` of `PagingData<Articles>`, allowing for reactive consumption of data as it becomes available.
     *
     * The paging configuration is set to load pages with a size of 10 articles each, and placeholders
     * are disabled (meaning the UI will only show actual loaded data, not placeholders for unloaded items).
     *
     * The resulting `Flow` is also cached within the `viewModelScope`, which ensures that data remains
     * available during configuration changes and reduces redundant API calls when the UI recomposes.
     *
     * @return A `Flow` of `PagingData<Articles>`, representing a paginated stream of breaking news articles.
     */
    fun getNews(category: String) : Flow<PagingData<Articles>> {
        return Pager(
            config = PagingConfig(10, enablePlaceholders = false), pagingSourceFactory = {BreakingNewsSource(api,category)}
        ).flow.cachedIn(viewModelScope)
    }

    /**
     * Retrieves a paginated list of news articles based on a search query.
     *
     * This function uses the Paging library to efficiently load and display news articles
     * matching the provided search term.  It returns a `Flow` of `PagingData<Articles>`,
     * which can be collected by a UI component to observe and display the results. The
     * paging configuration is set to a page size of 20, with placeholders disabled. The
     * retrieved data is cached within the ViewModel's scope.
     *
     * @param search The search query string used to filter news articles.
     * @return A `Flow` of `PagingData<Articles>`, representing the paginated list of search results.
     */
    fun getSearchNews(search: String) : Flow<PagingData<Articles>> {
        return Pager(
            config = PagingConfig(20, enablePlaceholders = false), pagingSourceFactory = {SearchNewsSource(api,search)}
        ).flow.cachedIn(viewModelScope)
    }



    /**
     * Retrieves all articles from the local database and updates the [_NewsArticleState].
     *
     * This function launches a coroutine within the [viewModelScope] to perform the database operation asynchronously.
     * It first sets the [_NewsArticleState] to [NewsViewState.Loading] to indicate that data is being loaded.
     * Then, it attempts to retrieve the articles using the [NewsDao.getArticles] method from the database.
     * Upon successful retrieval, it updates the [_NewsArticleState] to [NewsViewState.Success] with the list of articles.
     * If an exception occurs during the retrieval process (e.g., database error), it prints the exception to the console.
     *
     * Note: Error handling is currently limited to printing the exception. In a production app, you would typically
     * handle the exception more gracefully (e.g., displaying an error message to the user).
     */
    fun getArticles(){
        viewModelScope.launch {
            _NewsArticleState.value = NewsViewState.Loading(true)
            try {
                val result = db.getDao().getArticles()
                println(result)
                _NewsArticleState.value = NewsViewState.Success(result)
            } catch (e:Exception){
                println(e)
            }
        }
    }

    fun getArticlesCategory(category: String){
        viewModelScope.launch {
            _NewsArticleState.value = NewsViewState.Loading(true)
            try {
                val result = db.getDao().getArticlesCategory(category = category)
                println(category)
                println(result)
                _NewsArticleState.value = NewsViewState.Success(result)
            } catch (e:Exception){
                println(e)
            }
        }
    }

    /**
     * Retrieves news articles from the local database that match the given search query.
     *
     * This function performs a search in the local database using the provided `searchQuery`.
     * It updates the `_NewsArticleState` LiveData to reflect the current state of the operation:
     *  - `NewsViewState.Loading(true)`:  Indicates that the search is in progress.
     *  - `NewsViewState.Success(articles)`:  Indicates that the search was successful and provides the list of matching articles.
     *  - If an exception occurs during the search, it is caught and printed to the console (Note: In a production app, handle exceptions more robustly).
     *
     * @param searchQuery The string used to search for articles in the database.
     */
    fun getSearchArticles(searchQuery: String){
        viewModelScope.launch {
            _NewsArticleState.value = NewsViewState.Loading(true)
            try {
                val result = db.getDao().searchArticles(searchQuery)
                _NewsArticleState.value = NewsViewState.Success(result)
            } catch (e:Exception){
                println(e)
            }
        }
    }


    /**
     * Retrieves a single news article from the local database based on its URL.
     *
     * This function fetches a news article from the database using the provided URL.
     * It updates the [_NewsArticleState] LiveData to reflect the different stages:
     *   - [NewsViewState.Loading] is emitted at the beginning of the operation.
     *   - [NewsViewState.SuccessSingle] is emitted with the retrieved article if found.
     *
     * In case of any exceptions during database access, the exception is printed to the console,
     * but the LiveData state is not updated with an error.  This could be improved to handle errors
     * more robustly (e.g., updating the state with an Error view state).
     *
     * @param url The URL of the news article to retrieve.
     */
    fun SingleGetArticles(url:String){
        viewModelScope.launch {
            _NewsArticleState.value = NewsViewState.Loading(true)
            try {
                val result = db.getDao().getSingleArticles(url)
                if (result != null) {
                    _NewsArticleState.value = NewsViewState.SuccessSingle(result)
                }
                println("Arty $result")
            } catch (e:Exception){
                println(e)
            }
        }
    }

    /**
     * Saves a list of articles to the local database.
     *
     * This function takes a list of [Articles] as input and attempts to insert them into the database.
     * It uses a coroutine scope ([viewModelScope]) to perform the database operation asynchronously,
     * ensuring that it doesn't block the main thread.  A try-catch block handles potential exceptions
     * during the database insertion process.  If the insertion is successful, it prints "Save" to the console.
     * If an exception occurs (e.g., database error), it catches the exception and prints the error message to the console.
     *
     * @param articles The list of [Articles] to be saved to the database.
     */
    fun saveArticles(articles: Articles) {
        viewModelScope.launch {
            try {
                db.getDao().insertArticles(articles)
                println("Save")
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    /**
     * Deletes articles from the local database.
     *
     * This function attempts to delete the provided [articles] from the database using the DAO.
     * It launches a coroutine in the [viewModelScope] to perform the deletion asynchronously
     * to avoid blocking the main thread.  Success or failure is logged to the console.
     *
     * @param articles The [Articles] object to be deleted from the database.
     */
    fun deleteArticles(articles: Articles) {
        viewModelScope.launch {
            try {
                db.getDao().deleteArticles(articles)
                println("Delete")
            } catch (e: Exception) {
                println(e)
            }
        }
    }



    sealed class NewsViewState(){
        object IDLE : NewsViewState()
        data class Loading(val isLoading : Boolean): NewsViewState()
        data class Error(val message:String): NewsViewState()
        data class Success(val articles: List<Articles>): NewsViewState()
        data class SuccessSingle(val articles: Articles): NewsViewState()

    }


}