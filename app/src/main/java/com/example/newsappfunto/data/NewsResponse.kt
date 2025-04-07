package com.androiddevs.mvvmnewsapp.models



import com.example.newsappfunto.data.Articles

data class NewsResponse(
    val articles: List<Articles>,
    val status: String,
    val totalResults: Int
)