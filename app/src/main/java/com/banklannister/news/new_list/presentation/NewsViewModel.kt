package com.banklannister.news.new_list.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.banklannister.news.core.domain.NewsRepository
import com.banklannister.news.core.domain.NewsResult
import kotlinx.coroutines.launch


class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    var state by mutableStateOf(NewsState())
        private set

    init {
        loadNews()
    }

    fun onAction(action: NewsAction) {
        when (action) {
            NewsAction.Paginate -> {
                paginate()
            }
        }
    }


    private fun loadNews() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )

            newsRepository.getNews().collect { newResult ->
                when (newResult) {
                    is NewsResult.Error -> {
                        state = state.copy(
                            isError = true
                        )
                    }

                    is NewsResult.Success -> {
                        state = state.copy(
                            isError = false,
                            articleList = newResult.data?.articles ?: emptyList(),
                            nextPage = newResult.data?.nextPage
                        )

                    }
                }
            }

            state = state.copy(
                isLoading = false
            )

        }
    }

    private fun paginate() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )

            newsRepository.paginate(state.nextPage).collect { newResult ->
                when (newResult) {
                    is NewsResult.Error -> {
                        state = state.copy(
                            isError = true
                        )
                    }

                    is NewsResult.Success -> {
                        val articles = newResult.data?.articles ?: emptyList()
                        state = state.copy(
                            isError = false,
                            articleList = state.articleList + articles,
                            nextPage = newResult.data?.nextPage
                        )

                    }
                }
            }

            state = state.copy(
                isLoading = false
            )

        }
    }

}