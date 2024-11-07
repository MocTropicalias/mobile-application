package com.tropicalias.api

import android.net.Uri
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type


class Retrofit {

    enum class ApiType(val url: String) {
        SQL("https://tropicalias-api-ghrd.onrender.com/"),
        NOSQL("https://mongo-api-wr7a.onrender.com/"),
        REDIS("https://redis-api.onrender.com/"),
        LANDING_PAGE("https://tropicalias.onrender.com/")
    }


    fun getRetrofitClient(api: ApiType): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(Uri::class.java, UriTypeAdapter())
            .create()

        return Retrofit.Builder()
            .baseUrl(api.url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }


    class UriTypeAdapter : JsonSerializer<Uri>, JsonDeserializer<Uri> {
        override fun serialize(
            src: Uri?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return JsonPrimitive(src?.toString())
        }

        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Uri {
            return Uri.parse(json?.asString)
        }
    }


}