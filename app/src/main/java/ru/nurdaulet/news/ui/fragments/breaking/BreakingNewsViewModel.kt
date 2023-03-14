package ru.nurdaulet.news.ui.fragments.breaking

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.nurdaulet.news.domain.models.NewsResponse
import ru.nurdaulet.news.domain.repository.NewsRepository
import ru.nurdaulet.news.app.NewsApplication
import ru.nurdaulet.news.util.Constants.COUNTRY_CODE
import ru.nurdaulet.news.util.Constants.listOfCategories
import ru.nurdaulet.news.util.Resource
import java.io.IOException
import javax.inject.Inject

class BreakingNewsViewModel @Inject constructor(
    app: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val categoryNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var categoryNewsPage = 1
    var categoryNewsResponse: NewsResponse? = null

    init {
        getBreakingNews(COUNTRY_CODE)
        getCategoryNews(COUNTRY_CODE, 0, false)
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    fun getCategoryNews(countryCode: String, category: Int, paginate: Boolean) =
        viewModelScope.launch {
            safeCategoryNewsCall(countryCode, category, paginate)
        }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsResponse = resultResponse
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        if (response.body() == null) {
            return Resource.Error("Server error for Breaking News")
        }
        return Resource.Error(response.message())
    }

    private fun handleCategoryNewsResponse(
        response: Response<NewsResponse>,
        paginate: Boolean
    ): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (paginate) {
                    categoryNewsPage++
                    if (categoryNewsResponse == null) {
                        categoryNewsResponse = resultResponse
                    } else {
                        val oldArticles = categoryNewsResponse?.articles
                        val newArticles = resultResponse.articles
                        oldArticles?.addAll(newArticles)
                    }
                } else {
                    categoryNewsPage = 1
                    categoryNewsResponse = resultResponse
                }
                return Resource.Success(categoryNewsResponse ?: resultResponse)
            }
        }
        if (response.body() == null) {
            return Resource.Error("Server request is null")
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeCategoryNewsCall(
        countryCode: String,
        category: Int,
        paginate: Boolean
    ) {
        categoryNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response =
                    newsRepository.getCategoryNews(
                        countryCode,
                        listOfCategories[category],
                        categoryNewsPage
                    )
                categoryNews.postValue(handleCategoryNewsResponse(response, paginate))
            } else {
                categoryNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> categoryNews.postValue(Resource.Error("Network Failure"))
                else -> categoryNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}