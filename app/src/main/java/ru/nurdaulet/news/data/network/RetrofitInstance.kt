package ru.nurdaulet.news.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nurdaulet.news.util.Constants.API_KEY
import ru.nurdaulet.news.util.Constants.BASE_URL

object RetrofitInstance {

    private val retrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request()
                val url = request.url.newBuilder()
                    .addQueryParameter("apiKey", API_KEY)
                    .build()
                chain.proceed(request.newBuilder().url(url).build())
            }
            .build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val api: NewsAPI by lazy {
        retrofit.create(NewsAPI::class.java)
    }
}
