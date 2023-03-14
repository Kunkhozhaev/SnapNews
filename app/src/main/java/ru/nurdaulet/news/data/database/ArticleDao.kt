package ru.nurdaulet.news.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(articleDbModel: ArticleDbModel): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<ArticleDbModel>>

    @Query("DELETE FROM articles WHERE title = :articleTitle")
    suspend fun deleteArticle(articleTitle: String?)
}