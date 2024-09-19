package com.tropicalias.api.api

import com.tropicalias.api.model.PostPage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiNoSQL {
    @GET("/posts/{id}")
    fun getPostsFromUser(@Path("id") id: Long): Call<PostPage>

    @POST("/posts/like/{id}")
    fun likePost(@Path("id") id: Long)

    @POST("/posts/unlike/{id}")
    fun unlikePost(@Path("id") id: Long)
}