package ru.nurdaulet.news.ui.fragments.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nurdaulet.news.domain.repository.NewsRepository
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    private var _editProfileStatus: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val editProfileStatus: LiveData<Resource<Any?>> get() = _editProfileStatus

    fun editProfileData(username: String) = viewModelScope.launch {
        _editProfileStatus.value = Resource.Loading()
        newsRepository.editProfileUsername(username,
            {
                _editProfileStatus.value = Resource.Success(null)
            },
            {
                _editProfileStatus.value = Resource.Error(it)
            }
        )
    }

    private var _uploadImageStatus: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val uploadImageStatus: LiveData<Resource<Any?>> get() = _uploadImageStatus

    fun uploadPicture(imageUri: Uri) = viewModelScope.launch {
        _uploadImageStatus.value = Resource.Loading()
        newsRepository.uploadProfilePicture(imageUri,
            {
                _uploadImageStatus.value = Resource.Success(null)
            },
            {
                _uploadImageStatus.value = Resource.Error(it)
            }
        )
    }
}