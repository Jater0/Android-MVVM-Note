package com.example.app_2.service

import com.example.app_2.model.Article
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PaoService {
    @GET("article_detail.php/{name}")
    fun getArticleDetail(@Query("id") id: Int, @Path("name") name: String): Single<Article>
}