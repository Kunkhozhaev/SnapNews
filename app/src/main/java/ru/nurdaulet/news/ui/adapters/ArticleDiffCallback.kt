package ru.nurdaulet.news.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.nurdaulet.news.domain.models.Article

class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}