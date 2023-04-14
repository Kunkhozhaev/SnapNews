package ru.nurdaulet.news.ui.fragments.saved_articles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nurdaulet.news.domain.models.Article
import ru.nurdaulet.news.domain.models.FirebaseArticle
import ru.nurdaulet.news.domain.repository.NewsRepository
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class SavedNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private var _getSavedArticlesState: MutableLiveData<Resource<List<FirebaseArticle>?>> = MutableLiveData()
    val getSavedArticlesState: LiveData<Resource<List<FirebaseArticle>?>> get() = _getSavedArticlesState

    fun getSavedNews() = viewModelScope.launch {
        _getSavedArticlesState.value = Resource.Loading()
        newsRepository.getSavedArticles(
            {
                _getSavedArticlesState.value = Resource.Success(it)
            },
            {
                _getSavedArticlesState.value = Resource.Error(it)
            })
    }
}