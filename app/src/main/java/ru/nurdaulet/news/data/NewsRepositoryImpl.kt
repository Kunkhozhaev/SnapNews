package ru.nurdaulet.news.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.nurdaulet.news.data.api.RetrofitInstance
import ru.nurdaulet.news.data.database.ArticleDao
import ru.nurdaulet.news.data.database.ArticleModelMapper
import ru.nurdaulet.news.domain.models.Article
import ru.nurdaulet.news.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val articleDao: ArticleDao,
    private val mapper: ArticleModelMapper,
) : NewsRepository {

    override suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    override suspend fun getCategoryNews(countryCode: String, category: String, pageNumber: Int) =
        RetrofitInstance.api.getCategoryNews(countryCode, category, pageNumber)

    override suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    override suspend fun upsert(article: Article) =
        articleDao.upsert(mapper.mapEntityToDbModel(article))

    override fun getSavedNews(): LiveData<List<Article>> = Transformations.map(
        articleDao.getAllArticles()
    ) {
        mapper.mapListDbModelToListEntity(it)
    }

    override suspend fun deleteArticle(article: Article) = articleDao.deleteArticle(article.title)
}