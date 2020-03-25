package com.sai.fabula.utils


import com.sai.fabula.State
import com.sai.fabula.api.model.NewsResult
import retrofit2.Response

typealias ArticlesResponse = Response<NewsResult>

typealias ArticleListState = State<List<com.sai.fabula.database.model.Article>>
