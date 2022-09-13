package com.route.newsc36.api

import com.route.newsc36.api.model.NewsResponse
import com.route.newsc36.api.model.SourcesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {

    @GET("v2/top-headlines/sources")
    fun getNewsSources(@Query("apiKey") apiKey: String): Call<SourcesResponse>

    //?apiKey=5909ae28122a471d8b0c237d5989cb73&sources=abc-news
    @GET("v2/everything")
    fun getNews(
        @Query("apiKey") apiKey: String,
        @Query("sources") sources: String
    ): Call<NewsResponse>
}