package ru.nurdaulet.news.ui.fragments.saved_articles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nurdaulet.news.domain.models.Article
import ru.nurdaulet.news.domain.repository.NewsRepository
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class SavedNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private var _deleteArticleState: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val deleteArticleState: LiveData<Resource<Any?>> get() = _deleteArticleState

    fun deleteArticle(article: Article) = viewModelScope.launch {
        _deleteArticleState.value = Resource.Loading()
        newsRepository.deleteArticle(article,
            {
                _deleteArticleState.value = Resource.Success(null)
            },
            {
                _deleteArticleState.value = Resource.Error(it)
            })
    }

    private var _saveArticleState: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val saveArticleState: LiveData<Resource<Any?>> get() = _saveArticleState

    fun saveArticle(article: Article) = viewModelScope.launch {
        _saveArticleState.value = Resource.Loading()
        newsRepository.saveArticle(article,
            {
                _saveArticleState.value = Resource.Success(null)
            },
            {
                _saveArticleState.value = Resource.Error(it)
            })
    }

    private var _getSavedArticlesState: MutableLiveData<Resource<List<Article>?>> =
        MutableLiveData()
    val getSavedArticlesState: LiveData<Resource<List<Article>?>> get() = _getSavedArticlesState

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