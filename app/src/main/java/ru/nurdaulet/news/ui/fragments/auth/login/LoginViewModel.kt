package ru.nurdaulet.news.ui.fragments.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nurdaulet.news.domain.repository.NewsRepository
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private var _login: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val loginStatus: LiveData<Resource<Any?>> get() = _login

    fun login(mail: String, password: String) = viewModelScope.launch{
        _login.value = Resource.Loading()
        newsRepository.login(mail, password,
            {
                _login.value = Resource.Success(null)
            },
            {
                _login.value = Resource.Error(it)
            })
    }
}