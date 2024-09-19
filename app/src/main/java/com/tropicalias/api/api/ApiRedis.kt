package com.tropicalias.api.api

import retrofit2.http.GET

interface ApiRedis {

    @GET("/incr")
    fun incr()

    @GET("/decr")
    fun decr()

}