package ru.nurdaulet.news.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.firebase.auth.AuthCredential
import ru.nurdaulet.news.data.database.ArticleDao
import ru.nurdaulet.news.data.database.ArticleModelMapper
import ru.nurdaulet.news.data.network.AuthFirebase
import ru.nurdaulet.news.data.network.ProfileFirebase
import ru.nurdaulet.news.data.network.RetrofitInstance
import ru.nurdaulet.news.domain.models.Article
import ru.nurdaulet.news.domain.models.User
import ru.nurdaulet.news.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val articleDao: ArticleDao,
    private val mapper: ArticleModelMapper,
    private val auth: AuthFirebase,
    private val profile: ProfileFirebase,
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

    override suspend fun googleSignIn(
        credentials: AuthCredential,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        auth.googleSignIn(credentials, onSuccess, onFailure)
    }

    override suspend fun signUp(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        auth.signUp(email, password, onSuccess, onFailure)
    }

    override suspend fun addUserToDb(
        username: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        auth.addUserToDb(username, onSuccess, onFailure)
    }

    override suspend fun getProfileData(
        onSuccess: (user: User) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        profile.getProfileData(onSuccess, onFailure)
    }
}