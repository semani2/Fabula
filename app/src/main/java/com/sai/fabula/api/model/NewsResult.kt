package com.sai.fabula.api.model

import com.squareup.moshi.Json

data class NewsResult(
    @field:Json(name = "status") var status: String? = null,
    @field:Json(name = "totalResults") var totalResults: Int? = null,
    @field:Json(name = "articles") var articles: List<Article>? = null
)
