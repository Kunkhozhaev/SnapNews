package ru.nurdaulet.news.domain.repository

import androidx.lifecycle.LiveData
import retrofit2.Response
import ru.nurdaulet.news.domain.models.Article
import ru.nurdaulet.news.domain.models.NewsResponse

interface NewsRepository {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse>

    suspend fun getCategoryNews(
        countryCode: String,
        category: String,
        pageNumber: Int
    ): Response<NewsResponse>

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse>

    suspend fun upsert(article: Article): Long

    fun getSavedNews(): LiveData<List<Article>>

    suspend fun deleteArticle(article: Article)
}