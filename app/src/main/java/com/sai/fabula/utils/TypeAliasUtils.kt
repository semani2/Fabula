package com.sai.fabula.utils


import com.sai.fabula.State
import com.sai.fabula.api.model.Article
import retrofit2.Response

typealias ArticlesResponse = Response<List<Article>>

typealias ArticleListState = State<List<com.sai.fabula.database.model.Article>>
