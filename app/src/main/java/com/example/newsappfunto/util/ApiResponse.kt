package com.example.newsappfunto.util

data class SimpleApiResponse<D>( val status: String, val totalResults: Int, val articles:D)

data class info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)