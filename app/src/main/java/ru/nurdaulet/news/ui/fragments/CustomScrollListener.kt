package ru.nurdaulet.news.ui.fragments

import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.nurdaulet.news.util.Constants

abstract class CustomScrollListener(
    private val isLoading: Boolean,
    private val isLastPage: Boolean
) : RecyclerView.OnScrollListener() {
    /*var isLoading = false
    var isLastPage = false*/
    var isScrolling = false
    var shouldPaginate: Boolean = false


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
        val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
        shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem &&
                isNotAtBeginning && isTotalMoreThanVisible && isScrolling
    }
}