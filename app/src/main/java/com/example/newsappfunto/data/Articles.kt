package com.example.newsappfunto.data

import com.androiddevs.mvvmnewsapp.models.Source

data class Articles(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)
