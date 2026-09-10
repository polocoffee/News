package com.banklannister.news.new_list.presentation

sealed interface NewsAction {
    data object Paginate: NewsAction
}