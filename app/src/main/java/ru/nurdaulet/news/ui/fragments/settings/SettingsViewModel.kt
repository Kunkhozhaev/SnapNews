package ru.nurdaulet.news.ui.fragments.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nurdaulet.news.domain.repository.NewsRepository
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private var _editCountryStatus: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val editCountryStatus: LiveData<Resource<Any?>> get() = _editCountryStatus

    fun editCountry(countryCode: String) = viewModelScope.launch {
        _editCountryStatus.value = Resource.Loading()
        newsRepository.editCountry(countryCode,
            {
                _editCountryStatus.value = Resource.Success(null)
            },
            {
                _editCountryStatus.value = Resource.Error(it)
            }
        )
    }
}