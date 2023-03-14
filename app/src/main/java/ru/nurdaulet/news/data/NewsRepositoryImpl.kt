package ru.nurdaulet.news.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.nurdaulet.news.data.network.RetrofitInstance
import ru.nurdaulet.news.data.database.ArticleDao
import ru.nurdaulet.news.data.database.ArticleModelMapper
import ru.nurdaulet.news.data.network.AuthFirebase
import ru.nurdaulet.news.domain.models.Article
import ru.nurdaulet.news.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val articleDao: ArticleDao,
    private val mapper: ArticleModelMapper,
    private val auth: AuthFirebase,
) : NewsRepository {

    override suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    override suspend fun getCategoryNews(countryCode: String, category: String, pageNumber: Int) =
        RetrofitInstance.api.getCategoryNews(countryCode, category, pageNumber)

    override suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    override suspend fun upsert(article: Article) =
        articleDao.upsert(mapper.mapEntityToDbModel(article))

    override fun getSavedNews(): LiveData<List<Article>> = articleDao.getAllArticles().map {
        mapper.mapListDbModelToListEntity(it)
    }
    override suspend fun deleteArticle(article: Article) = articleDao.deleteArticle(article.title)

    override suspend fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        auth.login(email, password, onSuccess, onFailure)
    }

    override suspend fun signUp(
        username: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        auth.signUp(username, email, password, onSuccess, onFailure)
    }
}