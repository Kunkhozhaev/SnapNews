package ru.nurdaulet.news.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.nurdaulet.news.R
import ru.nurdaulet.news.domain.models.Article

class BreakingNewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val ivBreakingNews: ImageView = itemView.findViewById(R.id.ivBreakingNews)
    private val tvBreakingNewsTitle: TextView = itemView.findViewById(R.id.tvBreakingNewsTitle)

    fun onBind(article: Article, clickListener: ((Article) -> Unit)?) {

        Glide.with(itemView)
            .load(article.urlToImage)
            .placeholder(R.drawable.progress_download)
            .error(R.drawable.no_source_picture)
            .into(ivBreakingNews)

        tvBreakingNewsTitle.text = article.title

        itemView.setOnClickListener {
            clickListener?.let { it(article) }
        }
    }
}