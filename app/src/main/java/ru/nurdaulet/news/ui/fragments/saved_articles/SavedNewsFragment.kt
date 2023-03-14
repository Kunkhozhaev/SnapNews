package ru.nurdaulet.news.ui.fragments.saved_articles

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import ru.nurdaulet.news.ui.ViewModelFactory
import ru.nurdaulet.news.ui.adapters.NewsAdapter
import ru.nurdaulet.news.ui.fragments.FragmentGlobalContainer
import javax.inject.Inject

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val component by lazy{
        (requireActivity().application as NewsApplication).component
    }

    private lateinit var viewModel: SavedNewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var parentNavController: NavController

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

        parentNavController =
            (parentFragment?.parentFragment as FragmentGlobalContainer).findNavController()
        setupRecyclerView()
        onSwipeListener(view)
        setupGetSavedNewsObserve()

        newsAdapter.setOnArticleClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article", article)
            }
            parentNavController.navigate(
                R.id.action_fragmentGlobalContainer_to_articleFragment,
                bundle
            )
        }
    }

    private fun onSwipeListener(view: View) {
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
                val position = viewHolder.adapterPosition
                val article = newsAdapter.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAnchorView(R.id.bottomNavigationView)
                    setAction("Undo") {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }
    }

    private fun setupGetSavedNewsObserve() {
        viewModel.getSavedNews().observe(viewLifecycleOwner) { articles ->
            val distinctArticles = articles.distinctBy {
                it.title
            }
            newsAdapter.submitList(distinctArticles)
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()

        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}