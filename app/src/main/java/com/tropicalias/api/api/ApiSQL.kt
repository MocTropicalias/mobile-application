package com.tropicalias.api.api

import com.tropicalias.api.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiSQL {

    @POST("/user")
    fun createUser(@Body user: User): Call<User>

    @PATCH("/user/photo/{id}/{photo}")
    fun updateUserPhotoByFirebaseID(
        @Path("id") id: String,
        @Path("photo") photo: String
    ): Call<User>

    @GET("/user/firebase/{uid}")
    fun getUserByFirebaseID(@Path("uid") uid: String): Call<User>


}