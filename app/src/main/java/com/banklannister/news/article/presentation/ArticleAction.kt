package com.banklannister.news.article.presentation

sealed interface ArticleAction {
    data class LoadArticleId(val articleId: String): ArticleAction
}