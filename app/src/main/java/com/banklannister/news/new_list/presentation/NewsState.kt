package com.banklannister.news.new_list.presentation

import com.banklannister.news.core.domain.Article

data class NewsState(
    val articleList: List<Article> = emptyList(),
    val nextPage: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
