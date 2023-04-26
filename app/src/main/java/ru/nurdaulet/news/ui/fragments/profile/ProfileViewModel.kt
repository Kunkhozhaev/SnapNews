package ru.nurdaulet.news.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nurdaulet.news.domain.repository.NewsRepository
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private var _signOutStatus: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val signOutStatus: LiveData<Resource<Any?>> get() = _signOutStatus

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