package com.tropicalias.api.api

import retrofit2.Call
import retrofit2.http.GET

interface ApiRedis {

    @GET("/incr")
    fun incr(): Call<Unit>

    @GET("/decr")
    fun decr(): Call<Unit>

}