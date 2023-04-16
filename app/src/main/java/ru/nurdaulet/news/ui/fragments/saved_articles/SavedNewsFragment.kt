package ru.nurdaulet.news.ui.fragments.saved_articles

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.nurdaulet.news.R
import ru.nurdaulet.news.app.NewsApplication
import ru.nurdaulet.news.databinding.FragmentSavedNewsBinding
import ru.nurdaulet.news.domain.models.Article
import ru.nurdaulet.news.ui.ViewModelFactory
import ru.nurdaulet.news.ui.adapters.NewsAdapter
import ru.nurdaulet.news.ui.fragments.FragmentGlobalContainer
import ru.nurdaulet.news.ui.fragments.FragmentGlobalContainerDirections
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val component by lazy {
        (requireActivity().application as NewsApplication).component
    }

    private lateinit var viewModel: SavedNewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var parentNavController: NavController
    private lateinit var articleGlobalVariable: Article

    private var _binding: FragmentSavedNewsBinding? = null
    private val binding: FragmentSavedNewsBinding
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
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[SavedNewsViewModel::class.java]
        viewModel.getSavedNews()
        parentNavController =
            (parentFragment?.parentFragment as FragmentGlobalContainer).findNavController()
        setupRecyclerView()
        onSwipeListener()
        setupObservers()

        newsAdapter.setOnArticleClickListener { article ->
            parentNavController.navigate(
                FragmentGlobalContainerDirections.actionFragmentGlobalContainerToArticleFragment(
                    article
                )
            )
        }
    }

    private fun onSwipeListener() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.d("SavedNewsFragment", "I am onSwiped")
                val position = viewHolder.adapterPosition
                articleGlobalVariable = newsAdapter.currentList[position]
                viewModel.deleteArticle(articleGlobalVariable)
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }
    }

    private fun setupObservers() {
        viewModel.getSavedArticlesState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    setLoading(false)
                    response.data?.let { articles ->
                        val distinctArticles = articles.distinctBy {
                            it.title
                        }
                        newsAdapter.submitList(distinctArticles)
                    }
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
        viewModel.deleteArticleState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    setLoading(false)
                    viewModel.getSavedNews()
                    // TODO (Snackbar Spam)
                    Snackbar.make(
                        binding.root,
                        "Successfully deleted article",
                        Snackbar.LENGTH_SHORT
                    ).apply {
                        setAnchorView(R.id.bottomNavigationView)
                        setAction("Undo") {
                            viewModel.saveArticle(articleGlobalVariable)
                        }
                        show()
                    }
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
        viewModel.saveArticleState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    setLoading(false)
                    viewModel.getSavedNews()
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

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()

        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
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