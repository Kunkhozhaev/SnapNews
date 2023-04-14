package ru.nurdaulet.news.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.nurdaulet.news.domain.models.Source
import java.io.Serializable

@Entity(
    tableName = "articles"
)
data class ArticleDbModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String,
    val urlToImage: String?
) : Serializable