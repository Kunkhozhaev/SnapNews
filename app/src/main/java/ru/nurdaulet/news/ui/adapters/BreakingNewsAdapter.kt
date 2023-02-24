package ru.nurdaulet.news.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.nurdaulet.news.R
import ru.nurdaulet.news.domain.models.Article

class BreakingNewsAdapter : ListAdapter<Article, BreakingNewsViewHolder>(ArticleDiffCallback()) {

    private var onArticleClickListener: ((Article) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreakingNewsViewHolder {
        return BreakingNewsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_top_news_preview,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: BreakingNewsViewHolder, position: Int) {
        holder.onBind(currentList[position], onArticleClickListener)
    }

    fun setOnArticleClickListener(listener: (Article) -> Unit) {
        onArticleClickListener = listener
    }

}