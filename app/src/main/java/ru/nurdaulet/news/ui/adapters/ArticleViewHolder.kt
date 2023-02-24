package ru.nurdaulet.news.ui.adapters

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.nurdaulet.news.R
import ru.nurdaulet.news.domain.models.Article
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val ivArticleImage: ImageView = itemView.findViewById(R.id.ivArticleImage)
    private val tvSource: TextView = itemView.findViewById(R.id.tvSource)
    private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    private val tvPublishedAt: TextView = itemView.findViewById(R.id.tvPublishedAt)

    fun onBind(article: Article, clickListener: ((Article) -> Unit)?) {

        Glide.with(itemView)
            .load(article.urlToImage)
            .placeholder(R.drawable.progress_download)
            .error(R.drawable.no_source_picture)
            .into(ivArticleImage)

        tvSource.text = article.source?.name
        tvTitle.text = article.title
        tvPublishedAt.text = setTimeFormat(article.publishedAt)

        itemView.setOnClickListener {
            clickListener?.let { it(article) }
        }
    }

    private fun setTimeFormat(timeStamp: String?): String {
        val secondApiFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        val date = LocalDate.parse(timeStamp, secondApiFormat)

        val monthText = when (date.month.toString()) {
            "JANUARY" -> "Jan."
            "FEBRUARY" -> "Feb."
            "MARCH" -> "Mar."
            "APRIL" -> "Apr."
            "MAY" -> "May"
            "JUNE" -> "Jun."
            "JULY" -> "Jul."
            "AUGUST" -> "Aug."
            "SEPTEMBER" -> "Sept."
            "OCTOBER" -> "Oct."
            "NOVEMBER" -> "Nov."
            "DECEMBER" -> "Dec."
            else -> null
        }
        val dayText = date.dayOfMonth
        val yearText = date.year

        return "$monthText $dayText, $yearText"
    }
}