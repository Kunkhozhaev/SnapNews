package ru.nurdaulet.news.ui.fragments.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nurdaulet.news.domain.repository.NewsRepository
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private var _signIn: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val sigInStatus: LiveData<Resource<Any?>> get() = _signIn

    fun signUp(username: String, mail: String, password: String) = viewModelScope.launch{
        _signIn.value = Resource.Loading()
        newsRepository.signUp(username, mail, password,
            {
                _signIn.value = Resource.Success(null)
            },
            {
                _signIn.value = Resource.Error(it)
            })
    }
}