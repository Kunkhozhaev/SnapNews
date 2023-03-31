package ru.nurdaulet.news.ui.fragments.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.launch
import ru.nurdaulet.news.domain.repository.NewsRepository
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private var _login: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val loginStatus: LiveData<Resource<Any?>> get() = _login

    private var _googleSignIn: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val googleSignInStatus: LiveData<Resource<Any?>> get() = _googleSignIn

    private var _userAddStatus: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val userAddStatus: LiveData<Resource<Any?>> get() = _userAddStatus

    fun login(mail: String, password: String) = viewModelScope.launch {
        _login.value = Resource.Loading()
        newsRepository.login(mail, password,
            {
                _login.value = Resource.Success(null)
            },
            {
                _login.value = Resource.Error(it)
            })
    }
    fun googleSignIn(credential: AuthCredential) = viewModelScope.launch {
        _googleSignIn.value = Resource.Loading()
        newsRepository.googleSignIn(credential,
            {
                _googleSignIn.value = Resource.Success(null)
            },
            {
                _googleSignIn.value = Resource.Error(it)
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