package ru.nurdaulet.news.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.nurdaulet.news.di.ApplicationScope
import javax.inject.Provider

@ApplicationScope
class ViewModelFactory(
//    val app: Application,
//    val newsRepository: NewsRepository
    private val viewModelProviders: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //return NewsViewModel(app, newsRepository) as T
        return viewModelProviders[modelClass]?.get() as T
    }
}