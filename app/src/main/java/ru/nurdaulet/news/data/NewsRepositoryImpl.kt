package ru.nurdaulet.news.data

import android.net.Uri
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import ru.nurdaulet.news.data.network.ArticleFirebase
import ru.nurdaulet.news.data.network.AuthFirebase
import ru.nurdaulet.news.data.network.ProfileFirebase
import ru.nurdaulet.news.data.network.RetrofitInstance
import ru.nurdaulet.news.domain.models.Article
import ru.nurdaulet.news.domain.models.User
import ru.nurdaulet.news.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val auth: AuthFirebase,
    private val profile: ProfileFirebase,
    private val articles: ArticleFirebase
) : NewsRepository {

    override suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    override suspend fun uploadProfilePicture(
        imageUri: Uri,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        profile.uploadProfilePicture(imageUri, onSuccess, onFailure)
    }

    override suspend fun getCategoryNews(countryCode: String, category: String, pageNumber: Int) =
        RetrofitInstance.api.getCategoryNews(countryCode, category, pageNumber)

    override suspend fun editProfileUsername(
        username: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        profile.editProfileUsername(username, onSuccess, onFailure)
    }

    override suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    override suspend fun deleteArticle(
        article: Article,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        articles.deleteArticle(article, onSuccess, onFailure)
    }

    override suspend fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        auth.login(email, password, onSuccess, onFailure)
    }

    override suspend fun saveArticle(
        article: Article,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        articles.saveArticleToDb(article, onSuccess, onFailure)
    }

    override suspend fun getSavedArticles(
        onSuccess: (articles: List<Article>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        articles.getSavedArticles(onSuccess, onFailure)
    }

    override suspend fun googleSignIn(
        account: GoogleSignInAccount,
        signInClient: GoogleSignInClient,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        auth.googleSignIn(account, signInClient, onSuccess, onFailure)
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

    override suspend fun signOut(onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit) {
        auth.signOut(onSuccess, onFailure)
    }
}