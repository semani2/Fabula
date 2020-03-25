package com.sai.fabula.model

import androidx.room.PrimaryKey

data class Article(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String? = null,
    var description: String? = null,
    var author: String? = null,
    var source: String? = null,
    var url: String? = null,
    var imageUrl: String? = null
)
