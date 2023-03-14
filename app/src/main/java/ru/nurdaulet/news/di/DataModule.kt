package ru.nurdaulet.news.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.nurdaulet.news.data.NewsRepositoryImpl
import ru.nurdaulet.news.data.database.ArticleDao
import ru.nurdaulet.news.data.database.ArticleDatabase
import ru.nurdaulet.news.domain.repository.NewsRepository

@Module
interface DataModule {

    @Binds
    fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository

    companion object {

        @ApplicationScope
        @Provides
        fun providesArticleDao(
            application: Application
        ): ArticleDao {
            return ArticleDatabase.getInstance(application).getArticleDao()
        }
    }
}