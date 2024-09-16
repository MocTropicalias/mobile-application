package com.tropicalias.api.api

import com.tropicalias.api.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiSQL {

    @GET("/users/{id}")
    fun getUser(@Path("id") id: String): User

    @GET("/users")
    fun getAllUsers(): List<User>

    @POST("/users")
    fun createUser(@Body user: User): User

    @PATCH("/users/photo/{id}/{photo}")
    fun updateUserPhotoByFirebaseID(@Path("id") id: String, @Path("photo") photo: String): User

}