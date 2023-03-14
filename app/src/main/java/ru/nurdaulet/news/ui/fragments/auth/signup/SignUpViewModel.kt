package ru.nurdaulet.news.ui.fragments.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import ru.nurdaulet.news.domain.repository.NewsRepository
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private var _signIn: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val sigInStatus: LiveData<Resource<Any?>> get() = _signIn

    val firebaseAuth = FirebaseAuth.getInstance()

    fun signUp(mail: String, password: String) {
        _signIn.value = Resource.Loading()
        firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _signIn.value = Resource.Success(null)
            } else {
                _signIn.value = Resource.Error(it.exception.toString())
            }
        }
    }
}