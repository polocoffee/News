package com.banklannister.news.article.di

import com.banklannister.news.article.presentation.ArticleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val articleModule = module {

    viewModel { ArticleViewModel(get()) }


}