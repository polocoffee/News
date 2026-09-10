package com.banklannister.news.new_list.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.banklannister.news.new_list.presentation.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val newsModule = module {
    viewModel { NewsViewModel(get()) }
}