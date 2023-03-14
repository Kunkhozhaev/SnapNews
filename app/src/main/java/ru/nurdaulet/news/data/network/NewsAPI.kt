package ru.nurdaulet.news.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.nurdaulet.news.domain.models.NewsResponse
import ru.nurdaulet.news.util.Constants.COUNTRY_CODE

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = COUNTRY_CODE,
        @Query("page")
        pageNumber: Int = 1,
    ): Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getCategoryNews(
        @Query("country")
        countryCode: String = COUNTRY_CODE,
        @Query("category")
        category: String,
        @Query("page")
        pageNumber: Int = 1,
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
    ): Response<NewsResponse>
}
