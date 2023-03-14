package ru.nurdaulet.news.data.database

import ru.nurdaulet.news.domain.models.Article
import javax.inject.Inject

class ArticleModelMapper @Inject constructor() {

    fun mapEntityToDbModel(article: Article): ArticleDbModel = ArticleDbModel(
        id = article.id,
        author = article.author,
        content = article.content,
        description = article.description,
        publishedAt = article.publishedAt,
        source = article.source,
        title = article.title,
        url = article.url,
        urlToImage = article.urlToImage,
    )

    fun mapDbModelToEntity(articleDbModel: ArticleDbModel): Article = Article(
        id = articleDbModel.id,
        author = articleDbModel.author,
        content = articleDbModel.content,
        description = articleDbModel.description,
        publishedAt = articleDbModel.publishedAt,
        source = articleDbModel.source,
        title = articleDbModel.title,
        url = articleDbModel.url,
        urlToImage = articleDbModel.urlToImage,
    )

    fun mapListDbModelToListEntity(list: List<ArticleDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}