package com.example.newsappfunto.dao

import androidx.room.TypeConverter
import com.androiddevs.mvvmnewsapp.models.Source
import com.google.gson.Gson

class SourceTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromSource(source: Source): String {
        return gson.toJson(source)
    }

    @TypeConverter
    fun toSource(json: String): Source {
        return gson.fromJson(json, Source::class.java)
    }
}
