package ru.nurdaulet.news.domain.repository

import android.net.Uri
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
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

    suspend fun saveArticle(
        article: Article,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    )

    suspend fun getSavedArticles(
        onSuccess: (articles: List<Article>) -> Unit,
        onFailure: (msg: String?) -> Unit
    )

    suspend fun deleteArticle(
        article: Article,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    )

    suspend fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    )

    suspend fun googleSignIn(
        account: GoogleSignInAccount,
        signInClient: GoogleSignInClient,
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

    suspend fun editProfileUsername(
        username: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    )

    suspend fun uploadProfilePicture(
        imageUri: Uri,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    )
}