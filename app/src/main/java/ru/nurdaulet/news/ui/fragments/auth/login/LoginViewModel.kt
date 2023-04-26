package ru.nurdaulet.news.ui.fragments.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.coroutines.launch
import ru.nurdaulet.news.domain.repository.NewsRepository
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private var _login: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val loginStatus: LiveData<Resource<Any?>> get() = _login


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

    private var _googleSignIn: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val googleSignInStatus: LiveData<Resource<Any?>> get() = _googleSignIn
    fun googleSignIn(account: GoogleSignInAccount, signInClient: GoogleSignInClient) =
        viewModelScope.launch {
            _googleSignIn.value = Resource.Loading()
            newsRepository.googleSignIn(account,
                signInClient,
                {
                    _googleSignIn.value = Resource.Success(null)
                },
                {
                    _googleSignIn.value = Resource.Error(it)
                })
        }

    private var _profileStatus: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val profileStatus: LiveData<Resource<Any?>> get() = _profileStatus

    fun getProfileData() = viewModelScope.launch {
        _profileStatus.value = Resource.Loading()
        newsRepository.getProfileData(
            {
                _profileStatus.value = Resource.Success(null)
            },
            {
                _profileStatus.value = Resource.Error(it)
            }
        )
    }
}