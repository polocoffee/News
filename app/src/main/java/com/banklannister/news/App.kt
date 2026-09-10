package com.banklannister.news

import android.app.Application
import com.banklannister.news.article.di.articleModule
import com.banklannister.news.core.di.coreModule
import com.banklannister.news.new_list.di.newsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                coreModule,
                newsModule,
                articleModule
            )
        }
    }
}