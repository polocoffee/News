package com.banklannister.news.core.presentation

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    data object News: Screen

    @Serializable
    data class Article(val articleId: String): Screen
}