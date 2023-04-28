package ru.nurdaulet.news.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ru.nurdaulet.news.domain.models.Article
import ru.nurdaulet.news.domain.models.FirebaseArticle
import ru.nurdaulet.news.domain.models.Source
import ru.nurdaulet.news.util.Constants
import java.util.UUID
import javax.inject.Inject

class ArticleFirebase @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {

    fun saveArticleToDb(
        article: Article,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val firebaseArticle = FirebaseArticle(
            auth.currentUser!!.uid,
            article.publishedAt,
            article.source?.name,
            article.title,
            article.url,
            article.urlToImage
        )
        db.collection(Constants.FIREBASE_SAVED_ARTICLES).document(UUID.randomUUID().toString())
            .set(firebaseArticle)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun deleteArticle(
        article: Article,
        onSuccess: () -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val userId = auth.currentUser!!.uid
        db.collection(Constants.FIREBASE_SAVED_ARTICLES)
            .whereEqualTo(Constants.FIREBASE_USER_ID_FIELD, userId)
            .whereEqualTo(Constants.FIREBASE_TITLE_FIELD, article.title)
            .get()
            .addOnSuccessListener {
                it.documents.map { doc ->
                    doc.reference.delete()
                }
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun getSavedArticles(
        onSuccess: (articles: List<Article>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val userId = auth.currentUser!!.uid
        val resultList = mutableListOf<Article>()
        db.collection(Constants.FIREBASE_SAVED_ARTICLES)
            .whereEqualTo(Constants.FIREBASE_USER_ID_FIELD, userId).get()
            .addOnSuccessListener {
                it.documents.map { doc ->
                    val fireArticle = doc.toObject(FirebaseArticle::class.java)
                    fireArticle?.let { article ->
                        resultList.add(
                            Article(
                                null,
                                null,
                                null,
                                null,
                                article.publishedAt,
                                Source(null, article.sourceName!!),
                                article.title,
                                article.url,
                                article.urlToImage
                            )
                        )
                    }
                }
                onSuccess.invoke(resultList)
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}