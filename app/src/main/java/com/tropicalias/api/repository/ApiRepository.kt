package com.tropicalias.api.repository

import com.tropicalias.api.Retrofit
import com.tropicalias.api.api.ApiNoSQL
import com.tropicalias.api.api.ApiRedis
import com.tropicalias.api.api.ApiSQL

class ApiRepository private constructor() {

    companion object {

        @Volatile
        private var instance: ApiRepository? = null // Volatile modifier is necessary

        fun getInstance() =
            instance ?: synchronized(this) { // synchronized to avoid concurrency problem
                instance ?: ApiRepository().also { instance = it }
            }
    }

    fun getSQL() =
        Retrofit().getRetrofitClient()
            .create(ApiSQL::class.java)

    fun getNoSQL() =
        Retrofit().getRetrofitClient("")
            .create(ApiNoSQL::class.java)

    fun getRedis() =
        Retrofit().getRetrofitClient(" ")
            .create(ApiRedis::class.java)

}