package ru.nurdaulet.news.domain.repository

import androidx.lifecycle.LiveData
import com.google.firebase.auth.AuthCredential
import retrofit2.Response
import ru.nurdaulet.news.domain.models.Article
import ru.nurdaulet.news.domain.models.NewsResponse
import ru.nurdaulet.news.domain.models.User

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

    suspend fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    )

    suspend fun googleSignIn(
        credentials: AuthCredential,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    )

    suspend fun signUp(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    )

    suspend fun addUserToDb(
        username: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    )

    suspend fun getProfileData(
        onSuccess: (user: User) -> Unit,
        onFailure: (msg: String?) -> Unit
    )

    suspend fun signOut(
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    )
}