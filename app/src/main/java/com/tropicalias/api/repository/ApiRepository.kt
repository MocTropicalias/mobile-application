package com.tropicalias.api.repository

import androidx.lifecycle.MutableLiveData
import com.tropicalias.api.Retrofit
import com.tropicalias.api.api.ApiNoSQL
import com.tropicalias.api.api.ApiRedis
import com.tropicalias.api.api.ApiSQL
import com.tropicalias.api.model.User

class ApiRepository private constructor() {

    companion object {
        @Volatile
        private var instance: ApiRepository? = null // Volatile modifier is necessary

        fun getInstance() =
            instance ?: synchronized(this) { // synchronized to avoid concurrency problem
                instance ?: ApiRepository().also { instance = it }
            }
    }

    var user: MutableLiveData<User> = MutableLiveData()
    var sqlAPi: ApiSQL? = getSQL()


    fun getSQL(): ApiSQL {
        if (sqlAPi != null) {
            return sqlAPi as ApiSQL
        }
        return Retrofit().getRetrofitClient()
            .create(ApiSQL::class.java)
    }
    fun getNoSQL() =
        Retrofit().getRetrofitClient("")
            .create(ApiNoSQL::class.java)

    fun getRedis() =
        Retrofit().getRetrofitClient(" ")
            .create(ApiRedis::class.java)

}