package com.sai.fabula.database

import com.sai.fabula.State
import com.sai.fabula.api.FabulaApiService
import com.sai.fabula.database.model.Article
import com.sai.fabula.utils.ArticlesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NewsRepository @Inject constructor(private val fabulaDbModule: FabulaNewsDatabase,
                                         private val fabulaApiModule: FabulaApiService) {

    fun getNews() = flow {
        emit(State.Loading<List<Article>>())

        try {
            val apiResponse = fetchFromApi()

            val newsApiResponse = apiResponse.body()

            if (apiResponse.isSuccessful && newsApiResponse?.status.equals("ok", true)
                && !newsApiResponse?.articles.isNullOrEmpty()) {
                saveRemoteData(newsApiResponse?.articles!!)
            } else {
                emit(State.error<List<Article>>(apiResponse.message()))
            }
        } catch (e: Exception) {
            emit(State.error<List<Article>>("Network error! Can't get latest news articles"))
            Timber.e(e)
        }

        val localNewsArticles = fetchFromDatabase()
        if (!localNewsArticles.isNullOrEmpty()) {
            emit(State.success(localNewsArticles))
        } else {
            emit(State.error<List<Article>>("Can't get articles from the database"))
            Timber.e("Can't get articles from the database")
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun fetchFromApi(): ArticlesResponse =
        fabulaApiModule
            .getNewsArticles()

    private suspend fun fetchFromDatabase() = fabulaDbModule
        .getArticlesDao()
        .getAllArticles()

    private suspend fun saveRemoteData(remoteArticles: List<com.sai.fabula.api.model.Article>) {
        fabulaDbModule
            .getArticlesDao()
            .deleteAllPosts()

        val articles = remoteArticles.map { apiArticle ->
            Article(
                title = apiArticle.title,
                description = apiArticle.description,
                author = apiArticle.author,
                url = apiArticle.url,
                imageUrl = apiArticle.urlToImage,
                source = apiArticle.source?.name
            )
        }

        fabulaDbModule
            .getArticlesDao()
            .insertArticles(articles)
    }
}
