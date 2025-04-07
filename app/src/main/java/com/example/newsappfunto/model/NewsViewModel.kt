package com.example.newsappfunto.model

import androidx.lifecycle.ViewModel
import com.example.newsappfunto.data.Articles
import com.example.newsappfunto.service.NewsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsappfunto.dao.NewsArticlesDatabase
import com.example.newsappfunto.util.ResultWrapper
import com.example.newsappfunto.util.safeApiCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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


    fun getNewsArticles(){
        viewModelScope.launch {
            _NewsListState.value = NewsViewState.Loading(true)
            try {
                when(val response = safeApiCall { api.getBreakingNews() }){
                    is ResultWrapper.Success -> {
                        println(response.value)
                        _NewsListState.value = NewsViewState.Loading(false)
                        _NewsListState.value = NewsViewState.Success(response.value.articles)
                    }
                    is ResultWrapper.GenericError -> {
                        println(response.error)
                        _NewsListState.value = NewsViewState.Loading(false)
                        _NewsListState.value = NewsViewState.Error(response.error)
                    }

                    else -> {}
                }
            } catch (e:Exception){
                println(e)
            }

        }
    }

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

    fun saveArticles(articles: Articles) {
        viewModelScope.launch {
            try {
                db.getDao().insertArticles(articles)
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    fun deleteArticles(articles: Articles) {
        viewModelScope.launch {
            try {
                db.getDao().deleteArticles(articles)
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
    }
}