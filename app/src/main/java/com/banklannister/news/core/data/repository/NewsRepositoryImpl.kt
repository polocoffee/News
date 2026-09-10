package com.banklannister.news.core.data.repository

import com.banklannister.news.core.data.local.ArticleDao
import com.banklannister.news.core.data.remote.NewsListDto
import com.banklannister.news.core.data.toArticle
import com.banklannister.news.core.data.toArticleEntity
import com.banklannister.news.core.data.toNewsList
import com.banklannister.news.core.domain.Article
import com.banklannister.news.core.domain.NewsList
import com.banklannister.news.core.domain.NewsRepository
import com.banklannister.news.core.domain.NewsResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepositoryImpl(
    private val httpClient: HttpClient,
    private val dao: ArticleDao
) : NewsRepository {

    private val tag = "NewsRepository: "
    private val baseUrl = "https://newsdata.io/api/1/latest"
    private val apiKey = "pub_890444c94c794e15afa54618a0ce0791"

    private suspend fun getLocalNews(nextPage: String?): NewsList {
        val localNews = dao.getArticleList()
        println(tag + "getLocalNes: " + localNews.size + " nextPage: " + nextPage)

        val newsList = NewsList(
            articles = localNews.map { it.toArticle() },
            nextPage = nextPage
        )
        return newsList
    }

    private suspend fun getRemoteNews(nextPage: String?): NewsList {
        val newsListDto: NewsListDto = httpClient.get(baseUrl) {
            parameter("apikey", apiKey)
            parameter("language", "en")
            if (nextPage != null) parameter("page", nextPage)
        }.body()

        println(tag + "getRemoteNews: " + newsListDto.results?.size + " nextPage: " + nextPage)
        return newsListDto.toNewsList()
    }


    override suspend fun getNews(): Flow<NewsResult<NewsList>> {
        return flow {
            val remoteNewsList = try {
                getRemoteNews(null)
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                println(tag + "getNews remote exception: " + e.message)
                null
            }

            remoteNewsList?.let {
                dao.clearDatabase()
                dao.upsertArticleList(remoteNewsList.articles.map { it.toArticleEntity() })
                emit(NewsResult.Success(getLocalNews(remoteNewsList.nextPage)))
                return@flow
            }

            val localNewsList = getLocalNews(null)
            if (localNewsList.articles.isNotEmpty()) {
                emit(NewsResult.Success(localNewsList))
                return@flow
            }

            emit(NewsResult.Error("No Data"))

        }
    }

    override suspend fun paginate(nextPage: String?): Flow<NewsResult<NewsList>> {

        return flow {
            val remoteNewsList = try {
                getRemoteNews(nextPage)
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                println(tag + "paginate remote exception: " + e.message)
                null
            }

            remoteNewsList?.let {
                dao.upsertArticleList(remoteNewsList.articles.map { it.toArticleEntity() })

                emit(NewsResult.Success(getLocalNews(remoteNewsList.nextPage)))
                return@flow
            }
        }
    }

    override suspend fun getArticle(articleId: String): Flow<NewsResult<Article>> {

        return flow {
            dao.getArticleId(articleId)?.let { article ->
                println(tag + "get local article " + article.articleId)
                emit(NewsResult.Success(article.toArticle()))
                return@flow
            }

            try {
                val remoteArticle: NewsListDto = httpClient.get(baseUrl) {
                    parameter("apikey", apiKey)
                    parameter("id", articleId)
                }.body()

                println(tag + "get article remote " + remoteArticle.results?.size)

                if (remoteArticle.results?.isNotEmpty() == true) {
                    emit(NewsResult.Success(remoteArticle.results[0].toArticle()))
                } else {
                    emit(NewsResult.Error("Can't load Article"))
                }


            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                println(tag + "article remote exception: " + e.message)
                emit(NewsResult.Error("Can't load Article"))
            }
        }
    }
}