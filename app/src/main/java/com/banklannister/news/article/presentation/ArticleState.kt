package com.banklannister.news.article.presentation

import com.banklannister.news.core.domain.Article

data class ArticleState(
    val article: Article? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
