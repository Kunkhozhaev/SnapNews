package ru.nurdaulet.news.ui.fragments.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import ru.nurdaulet.news.domain.repository.NewsRepository
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private var _login: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val loginStatus: LiveData<Resource<Any?>> get() = _login

    val firebaseAuth = FirebaseAuth.getInstance()

    fun login(mail: String, password: String) {
        _login.value = Resource.Loading()
        firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _login.value = Resource.Success(null)
            } else {
                _login.value = Resource.Error(it.exception.toString())
            }
        }
    }
}