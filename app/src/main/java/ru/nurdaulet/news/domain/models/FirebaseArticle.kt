package ru.nurdaulet.news.domain.models

data class FirebaseArticle(
    val userId: String? = null,
    val publishedAt: String? = null,
    val sourceName: String? = null,
    val title: String? = null,
    val url: String = "",
    val urlToImage: String? = null
)
