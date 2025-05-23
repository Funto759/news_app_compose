package com.example.newsappfunto.service

import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.example.newsappfunto.data.Articles
import com.example.newsappfunto.util.Constants.API_KEY
import com.example.newsappfunto.util.SimpleApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("category")
        category: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>


    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ):  Response<NewsResponse>
}