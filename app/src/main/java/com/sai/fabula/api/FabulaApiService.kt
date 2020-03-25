package com.sai.fabula.api

import com.sai.fabula.utils.ArticlesResponse
import retrofit2.http.GET

/**
 * Service to fetch news articles from the News Api
 */
interface FabulaApiService {

    @GET("/top-headlines?country=us&apiKey=bc3c5392175a47509e3de5c96024e920")
    suspend fun getNewsArticles(): ArticlesResponse

    companion object {
        const val NEWS_API_URL = "http://newsapi.org/v2/"
    }
}
