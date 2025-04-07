package com.example.newsappfunto.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newsappfunto.data.Articles
import com.example.newsappfunto.service.NewsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsappfunto.util.ResultWrapper
import com.example.newsappfunto.util.safeApiCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(val api: NewsApi): ViewModel(){

    var _NewsListState = MutableStateFlow<NewsViewState>(NewsViewState.IDLE)
    val NewsListState = _NewsListState.asStateFlow()


    fun getNewsArticles(){
        viewModelScope.launch {
            _NewsListState.value = NewsViewState.Loading(true)
            try {
                when(val response = safeApiCall { api.getBreakingNews() }){
                    is ResultWrapper.Success -> {
                        _NewsListState.value = NewsViewState.Loading(false)
                        _NewsListState.value = NewsViewState.Success(response.value.data)
                    }
                    is ResultWrapper.GenericError -> {
                        _NewsListState.value = NewsViewState.Loading(false)
                        _NewsListState.value = NewsViewState.Error(response.error)
                    }

                    else -> {}
                }
            } catch (e:Exception){
                println(e)
            } catch (e:HttpException){
                println(e)
            }

        }
    }



    sealed class NewsViewState(){
        object IDLE : NewsViewState()
        data class Loading(val isLoading : Boolean): NewsViewState()
        data class Error(val message:String): NewsViewState()
        data class Success(val articles:List<Articles>): NewsViewState()
    }
}