package com.example.newsappfunto.model

import android.icu.text.StringSearch
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



//    val screenState: StateFlow<NewsScreenState> = NewsListState.combine(_isRefreshing) { newsState, refreshing ->
//        when (newsState) {
//            is NewsViewState.Success -> NewsScreenState(articles = newsState.articles, isRefreshing = refreshing)
//            else -> NewsScreenState(isRefreshing = refreshing)
//        }
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = NewsScreenState()
//    )



//    fun onPullToRefreshTrigger() {
//        _isRefreshing.update { true }
//        viewModelScope.launch {
////            getNewsArticles()
//            getNews()
//            _isRefreshing.update { false }
//        }
//    }

    fun getNews() : Flow<PagingData<Articles>> {
        return Pager(
            config = PagingConfig(10, enablePlaceholders = false), pagingSourceFactory = {BreakingNewsSource(api)}
        ).flow.cachedIn(viewModelScope)
    }

    fun getSearchNews(search: String) : Flow<PagingData<Articles>> {
        return Pager(
            config = PagingConfig(20, enablePlaceholders = false), pagingSourceFactory = {SearchNewsSource(api,search)}
        ).flow.cachedIn(viewModelScope)
    }


//    fun getNewsArticles(){
//        viewModelScope.launch {
//            _NewsListState.value = NewsViewState.Loading(true)
//            try {
//                when(val response = safeApiCall { api.getBreakingNews() }){
//                    is ResultWrapper.Success -> {
//                        println(response.value)
//                        _NewsListState.value = NewsViewState.Loading(false)
//                        _NewsListState.value = NewsViewState.Success(response.value.articles)
//                    }
//                    is ResultWrapper.GenericError -> {
//                        println(response.error)
//                        _NewsListState.value = NewsViewState.Loading(false)
//                        _NewsListState.value = NewsViewState.Error(response.error)
//                    }
//
//                    else -> {}
//                }
//            } catch (e:Exception){
//                println(e)
//            }
//
//        }
//    }

    fun getArticles(){
        viewModelScope.launch {
            _NewsArticleState.value = NewsViewState.Loading(true)
            try {
                val result = db.getDao().getArticles()
                _NewsArticleState.value = NewsViewState.Success(result)
            } catch (e:Exception){
                println(e)
            }
        }
    }

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

data class NewsScreenState(
    val articles: List<Articles> = emptyList(),
    val isRefreshing: Boolean = false
)