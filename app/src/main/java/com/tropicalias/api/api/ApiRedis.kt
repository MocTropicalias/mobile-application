package com.tropicalias.api.api

import retrofit2.Call
import retrofit2.http.POST

interface ApiRedis {

    @POST("/contador/incr")
    fun incr(): Call<Unit>

    @POST("/contador/decr")
    fun decr(): Call<Unit>

}