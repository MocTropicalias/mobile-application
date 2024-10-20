package com.tropicalias.api.api

import com.tropicalias.api.model.Comment
import com.tropicalias.api.model.Post
import com.tropicalias.api.model.PostPage
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiNoSQL {
    @POST("/post/")
    fun createPost(@Body post: Post): Call<Post>

    @GET("/post/user/{id}")
    fun getPostsFromUser(@Path("id") id: Long): Call<PostPage>

    @GET("/post/")
    fun getPostPage(@Path("id") id: Long): Call<PostPage>

    @PATCH("/post/{id}")
    fun addComment(@Path("id") id: Long, @Body comment: Comment): Call<Unit>

    @PATCH("/post/{id_post}/{id_user}")
    fun likePost(@Path("id_post") idPost: Long, @Path("id_user") idUser: Long): Call<Post>

    @DELETE("/post/{id}")
    fun deletePost(@Path("id") id: Long): Call<Unit>

    @DELETE("/post/{id}/{comment_position}")
    fun deleteComment(
        @Path("id") id: Long,
        @Path("comment_position") commentPosition: Int
    ): Call<Unit>
}