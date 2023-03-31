package ru.nurdaulet.news.util

object Constants {
    // main api: 758e97e2b2bd4b4497f5cdf3c26c8a6d
    // 2nd api: df13f3a9fed848b19e03519de8217715
    // 3rd api: 159f852d7a4b4e8aa7716c149b68d61d
    const val API_KEY = "df13f3a9fed848b19e03519de8217715"
    const val BASE_URL = "https://newsapi.org"
    const val QUERY_PAGE_SIZE = 20
    //first "1" is to overcome int rounding and second "1" is that last response is empty
    const val PAGE_OFFSET = 1 + 1
    val listOfCategories  = mutableListOf(
        "business",
        "entertainment",
        "general",
        "health",
        "science",
        "sports",
        "technology"
    )
    const val COUNTRY_CODE = "us"
    const val FIREBASE_USERS = "USERS"

}