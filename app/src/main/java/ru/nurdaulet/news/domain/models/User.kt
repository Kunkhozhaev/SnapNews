package ru.nurdaulet.news.domain.models

data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val image: String = "",
    val country: String = ""
)