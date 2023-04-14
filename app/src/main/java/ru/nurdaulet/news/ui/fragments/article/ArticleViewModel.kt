package ru.nurdaulet.news.ui.fragments.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nurdaulet.news.domain.models.Article
import ru.nurdaulet.news.domain.repository.NewsRepository
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class ArticleViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

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
}