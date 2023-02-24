package ru.nurdaulet.news.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.nurdaulet.news.domain.models.NewsResponse

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "uk",
        @Query("page")
        pageNumber: Int = 1,
    ): Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getCategoryNews(
        @Query("country")
        countryCode: String = "uk",
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
