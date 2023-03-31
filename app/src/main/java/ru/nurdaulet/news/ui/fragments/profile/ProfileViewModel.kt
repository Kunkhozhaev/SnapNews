package ru.nurdaulet.news.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nurdaulet.news.domain.models.User
import ru.nurdaulet.news.domain.repository.NewsRepository
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private var _profileStatus: MutableLiveData<Resource<User>> = MutableLiveData()
    val profileStatus: LiveData<Resource<User>> get() = _profileStatus

    private var _signOutStatus: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val signOutStatus: LiveData<Resource<Any?>> get() = _signOutStatus

    fun getProfileData() = viewModelScope.launch {
        _profileStatus.value = Resource.Loading()
        newsRepository.getProfileData(
            {
                _profileStatus.value = Resource.Success(it)
            },
            {
                _profileStatus.value = Resource.Error(it)
            }
        )
    }

    fun signOut() = viewModelScope.launch {
        _signOutStatus.value = Resource.Loading()
        newsRepository.signOut(
            {
                _signOutStatus.value = Resource.Success(null)
            },
            {
                _signOutStatus.value = Resource.Error(it)
            }
        )
    }
}