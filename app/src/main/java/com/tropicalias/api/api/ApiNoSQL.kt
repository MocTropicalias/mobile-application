package com.tropicalias.api.api

import com.tropicalias.api.model.Comment
import com.tropicalias.api.model.Post
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiNoSQL {
    @POST("/post/")
    fun createPost(@Body post: Post): Call<Post>

    @GET("/post/user/{id}")
    fun getPostsFromUser(@Path("id") id: Long): Call<List<Post>>

    @GET("/post/")
    fun getPosts(): Call<List<Post>>

    @GET("/post/{id}")
    fun getPostById(@Path("id") id: String): Call<Post>

    @PATCH("/post/{id}")
    fun addComment(@Path("id") id: Long, @Body comment: Comment): Call<Unit>

    @PATCH("/post/{id_post}/{id_user}")
    fun toggleLikePost(@Path("id_post") idPost: String, @Path("id_user") idUser: Long): Call<Int>

    @DELETE("/post/{id}")
    fun deletePost(@Path("id") id: Long): Call<Unit>

    @DELETE("/post/{id}/{comment_position}")
    fun deleteComment(
        @Path("id") id: Long,
        @Path("comment_position") commentPosition: Int
    ): Call<Unit>

    @GET("post/searchPosts")
    fun searchPosts(
        @Query("text") text: String,
        @Query("userId") userId: Long? = null,
        @Query("following") following: Boolean? = null,
        @Query("liked") liked: Boolean? = null
    ): Call<List<Post>>

}