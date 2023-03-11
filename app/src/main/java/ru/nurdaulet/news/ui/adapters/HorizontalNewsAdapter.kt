package ru.nurdaulet.news.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.nurdaulet.news.R
import ru.nurdaulet.news.domain.models.Article

class HorizontalNewsAdapter : ListAdapter<Article, HorizontalNewsViewHolder>(ArticleDiffCallback()) {

    private var onArticleClickListener: ((Article) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalNewsViewHolder {
        return HorizontalNewsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_top_news_preview,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: HorizontalNewsViewHolder, position: Int) {
        holder.onBind(currentList[position], onArticleClickListener)
    }

    fun setOnArticleClickListener(listener: (Article) -> Unit) {
        onArticleClickListener = listener
    }

}