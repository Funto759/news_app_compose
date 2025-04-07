package com.example.newsappfunto.util

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        var attempt = 0
        val maxRetries = 3  // Number of retries for HTTP 429
        var waitTime = 1000L // Initial delay in milliseconds (1 sec)

        while (attempt < maxRetries) {
            try {
                return@withContext ResultWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                when (throwable) {
                    is IOException -> return@withContext ResultWrapper.GenericError(103, NO_CONNECTION)
                    is UnknownHostException -> return@withContext ResultWrapper.GenericError(101, NO_CONNECTION)
                    is SocketTimeoutException -> return@withContext ResultWrapper.GenericError(102, NO_CONNECTION)
                    is HttpException -> {
                        when (throwable.code()) {
                            429 -> {
                                // Check if the server sends a Retry-After header
                                val retryAfter = throwable.response()?.headers()?.get("Retry-After")?.toLongOrNull() ?: waitTime / 1000
                                println("Rate Limited (HTTP 429). Retrying in $retryAfter seconds...")

                                if (attempt < maxRetries - 1) {
                                    delay(retryAfter * 1000)  // Wait before retrying
                                    attempt++
                                    waitTime *= 2 // Exponential backoff
                                    continue
                                } else {
                                    return@withContext ResultWrapper.GenericError(429, "Too many requests. Please try again later.")
                                }
                            }
                            422 -> return@withContext ResultWrapper.GenericError(throwable.code(), parseErrorBody()(throwable))
                            else -> return@withContext ResultWrapper.GenericError(throwable.code(), "Something went wrong")
                        }
                    }
                    else -> return@withContext ResultWrapper.GenericError(null, throwable.localizedMessage ?: "Unknown Error")
                }
            }
        }

        // If we reach here, max retries have been reached
        return@withContext ResultWrapper.GenericError(429, "Request limit exceeded. Try again later.")
    }
}

fun fetchError(code: Int): String {
    if (code in 401..403) {
        return UNAUTHORIZED
    }
    return NOT_DEFINED
}

@Suppress("UNCHECKED_CAST")
fun getMeta(meta: MutableMap<String, *>?, key: String): Int? {
    val map = meta?.get("pagination") as HashMap<String, Int>
    return map[key]
}

fun parseErrorBody(): (Throwable) -> String {
    return { throwable ->
        when {
            throwable is HttpException -> {
                try {
                    val responseMap = Gson().fromJson(
                        throwable.response()?.errorBody()!!.string(),
                        HashMap::class.java
                    ) as HashMap<*, *>
                    "${(responseMap["message"])}"
                } catch (e: Exception) {
                    "genericResponse"
                }
            }
            else -> throwable.message ?: ""
        }
    }
}

const val NO_CONNECTION = "Not Connected To Internet"
const val UNAUTHORIZED = "You are Unauthorized to View This Page"
const val NOT_DEFINED = "Please Report Bug"
const val TIMEOUT = "Request has been Timed out"
const val EMPTY_RESPONSE = "no data available in repository"

