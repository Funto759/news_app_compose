package com.example.newsappfunto.di

import android.content.Context
import androidx.room.Room
import com.example.newsappfunto.dao.NewsArticlesDatabase
import com.example.newsappfunto.service.NewsApi
import com.example.newsappfunto.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): NewsArticlesDatabase {
        return Room.databaseBuilder(
            context, NewsArticlesDatabase::class.java,"News_Articles_Database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideDao(database: NewsArticlesDatabase) = database.getDao()



    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsAPiService(client: OkHttpClient):NewsApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()
            .create(NewsApi::class.java)

    }


}