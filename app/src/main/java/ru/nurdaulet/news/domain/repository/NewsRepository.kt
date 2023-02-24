package ru.nurdaulet.news.domain.repository

import ru.nurdaulet.news.data.api.RetrofitInstance
import ru.nurdaulet.news.data.database.ArticleDatabase
import ru.nurdaulet.news.domain.models.Article

class NewsRepository(
    private val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun getCategoryNews(countryCode: String, category: String, pageNumber: Int) =
        RetrofitInstance.api.getCategoryNews(countryCode, category, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article.title)
}