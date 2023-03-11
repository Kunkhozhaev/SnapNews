package ru.nurdaulet.news.data

import ru.nurdaulet.news.data.api.RetrofitInstance
import ru.nurdaulet.news.data.database.ArticleDatabase
import ru.nurdaulet.news.domain.models.Article
import ru.nurdaulet.news.domain.repository.NewsRepository

class NewsRepositoryImpl(
    private val db: ArticleDatabase
) : NewsRepository {

    override suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    override suspend fun getCategoryNews(countryCode: String, category: String, pageNumber: Int) =
        RetrofitInstance.api.getCategoryNews(countryCode, category, pageNumber)

    override suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    override suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    override fun getSavedNews() = db.getArticleDao().getAllArticles()

    override suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article.title)
}