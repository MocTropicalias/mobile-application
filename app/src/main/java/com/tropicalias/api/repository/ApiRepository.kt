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
    private var sqlAPi: ApiSQL? = getSQL()
    private var noSqlAPi: ApiNoSQL? = getNoSQL()
    private var resdisAPi: ApiRedis? = getRedis()


    fun getSQL(): ApiSQL {
        if (sqlAPi != null) {
            return sqlAPi as ApiSQL
        }
        return Retrofit().getRetrofitClient(Retrofit.ApiType.SQL)
            .create(ApiSQL::class.java)
    }

    fun getNoSQL(): ApiNoSQL {
        if (noSqlAPi != null) {
            return noSqlAPi as ApiNoSQL
        }
        return Retrofit().getRetrofitClient(Retrofit.ApiType.NOSQL)
            .create(ApiNoSQL::class.java)
    }

    fun getRedis(): ApiRedis {
        if (resdisAPi != null) {
            return resdisAPi as ApiRedis
        }
        return Retrofit().getRetrofitClient(Retrofit.ApiType.REDIS)
            .create(ApiRedis::class.java)

    }
}