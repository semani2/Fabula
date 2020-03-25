package com.sai.fabula.api.model

import com.squareup.moshi.Json

data class Source(
    @field:Json(name = "name") var name: String? = null
)
