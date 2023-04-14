package ru.nurdaulet.news.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ru.nurdaulet.news.domain.models.Article
import ru.nurdaulet.news.domain.models.FirebaseArticle
import ru.nurdaulet.news.util.Constants
import java.util.*
import javax.inject.Inject

class ArticleFirebase @Inject constructor(
    //private val auth: FirebaseAuth,
    // private val db: FirebaseFirestore
) {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

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

    fun getSavedArticles(
        onSuccess: (articles: List<FirebaseArticle>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val userId = auth.currentUser!!.uid
        val resultList = mutableListOf<FirebaseArticle>()
        db.collection(Constants.FIREBASE_SAVED_ARTICLES).get()
            .addOnSuccessListener {
                for (index in 0 until it.documents.size) {
                    if (it.documents[index].get(Constants.FIREBASE_USER_ID) == userId) {
                        val fireArticle = it.documents[index].toObject(FirebaseArticle::class.java)
                        fireArticle?.let { article ->
                            resultList.add(article)
                        }
                    }
                }
                onSuccess.invoke(resultList)
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}