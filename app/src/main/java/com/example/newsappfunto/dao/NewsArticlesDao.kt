package com.example.newsappfunto.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.newsappfunto.data.Articles

@Dao
interface NewsArticlesDao {

    @Upsert
    suspend fun insertArticles(articles: Articles):Long

    @Delete
    suspend fun deleteArticles(articles: Articles):Int

    @Query("SELECT * FROM NewsArticles")
    suspend fun getArticles():List<Articles>

    @Query("SELECT * FROM NewsArticles WHERE url = :url")
    suspend fun getSingleArticles(url:String):Articles

    @Query("SELECT * FROM NewsArticles WHERE title LIKE :query  || '%' OR description LIKE :query || '%' OR content LIKE :query || '%' ")
    suspend fun searchArticles(query:String): List<Articles>



}