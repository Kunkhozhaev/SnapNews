package ru.nurdaulet.news.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import ru.nurdaulet.news.data.NewsRepositoryImpl
import ru.nurdaulet.news.data.shared_pref.SharedPref
import ru.nurdaulet.news.data.shared_pref.SharedPrefImpl
import ru.nurdaulet.news.domain.repository.NewsRepository

@Module
interface DataModule {

    @Binds
    fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository

    @Binds
    fun bindSharedPref(impl: SharedPrefImpl): SharedPref

    @Binds
    fun bindContext(application: Application): Context
}