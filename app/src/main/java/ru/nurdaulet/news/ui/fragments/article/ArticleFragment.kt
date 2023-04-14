package ru.nurdaulet.news.ui.fragments.article

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import ru.nurdaulet.news.R
import ru.nurdaulet.news.app.NewsApplication
import ru.nurdaulet.news.databinding.FragmentArticleBinding
import ru.nurdaulet.news.ui.ViewModelFactory
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class ArticleFragment : Fragment(R.layout.fragment_article) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: ArticleViewModel
    private val args: ArticleFragmentArgs by navArgs()
    private val component by lazy{
        (requireActivity().application as NewsApplication).component
    }

    private var _binding: FragmentArticleBinding? = null
    private val binding: FragmentArticleBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        viewModel = ViewModelProvider(this, viewModelFactory)[ArticleViewModel::class.java]
        setupArticleSaveObserver()


        binding.apply {
            iconBack.setOnClickListener {
                findNavController().popBackStack()
            }
            ivShareBtn.setOnClickListener {
                val sendInfo =
                    "Check out these news: ${article.url}"

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, sendInfo)
                    type = "text/plain"
                }

                try {
                    startActivity(sendIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        requireActivity(),
                        "There is no right app to share",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
            ivSaveBtn.setOnClickListener {
                viewModel.saveArticle(article)
            }
            webView.apply {
                webViewClient = WebViewClient()
                loadUrl(article.url)
            }
        }
    }

    private fun setupArticleSaveObserver() {
        viewModel.saveArticleState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    setLoading(false)
                    Snackbar.make(binding.root, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    setLoading(false)
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Resource.Loading -> {
                    setLoading(true)
                }
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.apply {
            paginationProgressBar.isVisible = isLoading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}