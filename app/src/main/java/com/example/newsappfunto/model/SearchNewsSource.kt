package com.example.newsappfunto.model

import android.icu.text.StringSearch
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsappfunto.data.Articles
import com.example.newsappfunto.service.NewsApi
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchNewsSource @Inject constructor(private val api: NewsApi,val search: String):PagingSource<Int, Articles>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Articles> {
        val position = params.key ?: 1

        return try {
           val response = api.searchForNews(searchQuery = search,pageNumber = position)

            if (response.isSuccessful) {
                val body = response.body()
                LoadResult.Page(
                    data = body?.articles ?: emptyList(),
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = if (body?.totalResults != null) position + 1 else null
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        }catch (e:IOException){
            LoadResult.Error(e)
        }catch (e:HttpException){
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Articles>): Int? {
       return state.anchorPosition?.let { anchorPosition ->
           state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
               ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
       }
    }

    data class NoDataException(val errorMessage: String) : Exception(errorMessage)
}