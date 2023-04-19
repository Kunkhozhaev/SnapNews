package ru.nurdaulet.news.ui.fragments.breaking

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import ru.nurdaulet.news.R
import ru.nurdaulet.news.app.NewsApplication
import ru.nurdaulet.news.data.shared_pref.SharedPref
import ru.nurdaulet.news.databinding.FragmentBreakingNewsBinding
import ru.nurdaulet.news.ui.ViewModelFactory
import ru.nurdaulet.news.ui.adapters.HorizontalNewsAdapter
import ru.nurdaulet.news.ui.adapters.NewsAdapter
import ru.nurdaulet.news.ui.fragments.FragmentGlobalContainer
import ru.nurdaulet.news.ui.fragments.FragmentGlobalContainerDirections
import ru.nurdaulet.news.util.Constants.PAGE_OFFSET
import ru.nurdaulet.news.util.Constants.QUERY_PAGE_SIZE
import ru.nurdaulet.news.util.Resource
import javax.inject.Inject

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val component by lazy {
        (requireActivity().application as NewsApplication).component
    }

    private lateinit var viewModel: BreakingNewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var horizontalNewsAdapter: HorizontalNewsAdapter
    private lateinit var parentNavController: NavController
    private var tabPosition = 0

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding: FragmentBreakingNewsBinding
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
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[BreakingNewsViewModel::class.java]
        parentNavController =
            (parentFragment?.parentFragment as FragmentGlobalContainer).findNavController()
        Log.d("BreakingNewsFragment", sharedPref.country)
        viewModel.getBreakingNews(sharedPref.country)
        viewModel.getCategoryNews(sharedPref.country, 0, false)
        setupRecyclerView()
        setupBreakingNewsObserver()
        setupCategoryNewsObserver()
        adapterClickListeners()

        binding.ivSearchBtn.setOnClickListener {
            parentNavController.navigate(R.id.action_fragmentGlobalContainer_to_searchNewsFragment)
        }
        binding.tabCategoryLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabPosition = tab?.position ?: 0
                viewModel.getCategoryNews(sharedPref.country, tabPosition, false)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupBreakingNewsObserver() {
        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    setLoading(false)
                    response.data?.let { topNewsResponse ->
                        horizontalNewsAdapter.submitList(topNewsResponse.articles.toList())
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
    }

    private fun setupCategoryNewsObserver() {
        viewModel.categoryNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    setLoading(false)
                    response.data?.let { newsResponse ->
                        newsAdapter.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + PAGE_OFFSET
                        isLastPage = viewModel.categoryNewsPage == totalPages
                        if (isLastPage) {
                            binding.rvCategoryNews.setPadding(0, 0, 0, 0)
                        }
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
    }

    private fun adapterClickListeners() {
        horizontalNewsAdapter.setOnArticleClickListener { article ->
            parentNavController.navigate(
                FragmentGlobalContainerDirections.actionFragmentGlobalContainerToArticleFragment(
                    article
                )
            )
        }
        newsAdapter.setOnArticleClickListener { article ->
            parentNavController.navigate(
                FragmentGlobalContainerDirections.actionFragmentGlobalContainerToArticleFragment(
                    article
                )
            )
        }
    }

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    private val categoryArticlesScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem &&
                    isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getCategoryNews(sharedPref.country, tabPosition, true)
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        horizontalNewsAdapter = HorizontalNewsAdapter()

        binding.rvBreakingNews.apply {
            adapter = horizontalNewsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvCategoryNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(categoryArticlesScrollListener)
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