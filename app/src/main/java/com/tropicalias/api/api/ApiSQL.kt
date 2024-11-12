package com.tropicalias.api.api

import com.tropicalias.api.model.Color
import com.tropicalias.api.model.Event
import com.tropicalias.api.model.Follow
import com.tropicalias.api.model.Mascote
import com.tropicalias.api.model.Ticket
import com.tropicalias.api.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("/user/{id}")
    fun getUserProfileByID(@Path("id") id: Long): Call<User>

    @GET("/user")
    fun getAllUsers(): Call<List<User>>

    @GET("/user/authorization/{email}/{senha}")
    fun passwordMatch(@Path("email") email: String, @Path("senha") password: String): Call<Unit>


    @GET("/follow/countfollowers/{id}")
    fun getUserFollowersCount(@Path("id") id: String): Call<Int>

    @PUT("/user/{id}")
    fun updateUserProfile(@Body user: User, @Path("id") id: String): Call<User>

    @POST("/follow/{id_to_follow}/{id_following}")
    fun toggleFollowUser(
        @Path("id_to_follow") idToFollow: Long,
        @Path("id_following") idFollowing: Long
    ): Call<Follow>

    @GET("/follow/{id_to_follow}/{id_following}")
    fun getFollowUser(
        @Path("id_to_follow") idToFollow: Long,
        @Path("id_following") idFollowing: Long
    ): Call<Follow>

    @GET("follow/{id}")
    fun getUsersFollowing(@Path("id") id: Long): Call<List<User>>

    @POST("/ticket/")
    fun createTicket(@Query("idEvent") eventId: Long, @Query("idUser") userId: Long): Call<Ticket>

    @GET("/ticket/user/{userId}")
    fun getUserRegisteredEvents(@Path("userId") userId: Long): Call<List<Ticket>>

    @PATCH("/ticket/addTickets")
    fun addTickets(@Query("id") eventId: Long, @Query("tickets") ticketAmount: Int): Call<Ticket>

    @GET("/cor")
    fun getAllColors(): Call<List<Color>>

    @PUT("/mascote/")
    fun updateMascot(@Body mascot: Mascote): Call<Mascote>

    @GET("/mascote/usermascote/{userId}")
    fun getMascot(@Path("userId") userId: Long): Call<Mascote>

    @GET("/evento/{id}")
    fun getEvent(@Path("id") eventId: Long): Call<Event>

    @POST("/vendaevento")
    fun registrarVenda(
        @Query("barracaId") barracaId: Long,
        @Query("userId") userId: Long,
        @Query("qtTickets") qtTickets: Int
    ): Call<Unit>

}