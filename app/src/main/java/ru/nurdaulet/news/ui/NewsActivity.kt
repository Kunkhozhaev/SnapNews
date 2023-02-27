package ru.nurdaulet.news.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.nurdaulet.news.data.database.ArticleDatabase
import ru.nurdaulet.news.databinding.ActivityNewsBinding
import ru.nurdaulet.news.domain.repository.NewsRepository

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    private var _binding: ActivityNewsBinding? = null
    private val binding: ActivityNewsBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]
    }
}