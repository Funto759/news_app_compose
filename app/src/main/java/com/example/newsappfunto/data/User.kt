package com.example.newsappfunto.data

import android.net.Uri

data class User(
    val firstname: String = "" ,
    val lastname: String = "",
    val phoneNumber: String = "" ,
    val email: String = "",
    val image :Uri? = null
)
