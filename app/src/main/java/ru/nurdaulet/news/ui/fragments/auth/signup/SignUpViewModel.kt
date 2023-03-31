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

    private var _signUp: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val signUpStatus: LiveData<Resource<Any?>> get() = _signUp

    private var _userAddStatus: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val userAddStatus: LiveData<Resource<Any?>> get() = _userAddStatus

    fun signUp(mail: String, password: String) = viewModelScope.launch{
        _signUp.value = Resource.Loading()
        newsRepository.signUp(mail, password,
            {
                _signUp.value = Resource.Success(null)
            },
            {
                _signUp.value = Resource.Error(it)
            })
    }
    fun addUserToDb(username: String) = viewModelScope.launch {
        _userAddStatus.value = Resource.Loading()
        newsRepository.addUserToDb(username,
            {
                _userAddStatus.value = Resource.Success(null)
            },
            {
                _userAddStatus.value = Resource.Error(it)
            })
    }
}