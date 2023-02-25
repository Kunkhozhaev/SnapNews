package ru.nurdaulet.news.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import ru.nurdaulet.news.R
import ru.nurdaulet.news.data.database.ArticleDatabase
import ru.nurdaulet.news.databinding.ActivityNewsBinding
import ru.nurdaulet.news.domain.repository.NewsRepository
import ru.nurdaulet.news.ui.fragments.BreakingNewsFragmentDirections

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var navHostFragment: NavHostFragment

    private var _binding: ActivityNewsBinding? = null
    private val binding: ActivityNewsBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val customToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarWidget)
        setSupportActionBar(customToolbar)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.searchingNewsFragment -> navHostFragment.findNavController()
                .navigate(
                    BreakingNewsFragmentDirections.actionBreakingNewsFragmentToSearchNewsFragment()
                )
            else -> Toast.makeText(this, "nothing", Toast.LENGTH_SHORT).show()
        }
        return false
    }
}