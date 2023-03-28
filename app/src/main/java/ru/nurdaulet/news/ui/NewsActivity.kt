package ru.nurdaulet.news.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.nurdaulet.news.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {
    private var _binding: ActivityNewsBinding? = null
    private val binding: ActivityNewsBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}