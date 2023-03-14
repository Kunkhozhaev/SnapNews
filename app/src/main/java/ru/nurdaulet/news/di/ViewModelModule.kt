package ru.nurdaulet.news.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.nurdaulet.news.ui.fragments.article.ArticleViewModel
import ru.nurdaulet.news.ui.fragments.breaking.BreakingNewsViewModel
import ru.nurdaulet.news.ui.fragments.saved_articles.SavedNewsViewModel
import ru.nurdaulet.news.ui.fragments.search.SearchNewsViewModel

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(ArticleViewModel::class)
    @Binds
    fun bindArticleViewModel(impl: ArticleViewModel): ViewModel

    @IntoMap
    @ViewModelKey(BreakingNewsViewModel::class)
    @Binds
    fun bindBreakingNewsViewModel(impl: BreakingNewsViewModel): ViewModel

    @IntoMap
    @ViewModelKey(SavedNewsViewModel::class)
    @Binds
    fun bindSavedNewsViewModel(impl: SavedNewsViewModel): ViewModel

    @IntoMap
    @ViewModelKey(SearchNewsViewModel::class)
    @Binds
    fun bindSearchNewsViewModel(impl: SearchNewsViewModel): ViewModel
}