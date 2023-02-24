package ru.nurdaulet.news.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.nurdaulet.news.R
import ru.nurdaulet.news.domain.models.Article

class NewsAdapter : ListAdapter<Article, ArticleViewHolder>(ArticleDiffCallback()) {

    private var onArticleClickListener: ((Article) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.onBind(currentList[position], onArticleClickListener)
    }

    fun setOnArticleClickListener(listener: (Article) -> Unit) {
        onArticleClickListener = listener
    }

}