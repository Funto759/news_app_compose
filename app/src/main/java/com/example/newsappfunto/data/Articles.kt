package com.example.newsappfunto.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.androiddevs.mvvmnewsapp.models.Source

@Entity(tableName = "NewsArticles")
data class Articles(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)
