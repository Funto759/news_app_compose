package com.example.newsappfunto.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsappfunto.data.Articles


@Database(entities = [Articles::class], version = 1, exportSchema = false)
abstract class NewsArticlesDatabase :RoomDatabase(){
    abstract fun getDao(): NewsArticlesDao
}