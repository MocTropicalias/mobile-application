package com.tropicalias.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retrofit {

//    val BASE_URL = "https://tropicalias-api.onrender.com/"

    private val BASE_URL = "https://localhost:8080/"

    fun getRetrofitClient(url: String = BASE_URL): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}