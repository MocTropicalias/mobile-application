package com.tropicalias.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.tropicalias.api.model.Post
import com.tropicalias.api.model.User
import com.tropicalias.api.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Call
import retrofit2.Response
import java.nio.charset.StandardCharsets

class ApiHelper {
    companion object {

        fun bodyToString(requestBody: RequestBody?): String {
            return try {
                val buffer = Buffer()
                requestBody?.writeTo(buffer)
                buffer.readString(StandardCharsets.UTF_8)
            } catch (e: Exception) {
                "Unable to log request body"
            }
        }

        fun getUser(callback: ((user: User) -> Unit)? = null) {
            val repository = ApiRepository.getInstance()
            repository.user.value?.let { user ->
                if (callback != null) {
                    callback(user)
                }
            }

            repository.getSQL().getUserByFirebaseID(FirebaseAuth.getInstance().currentUser?.uid!!)
                .enqueue(object : retrofit2.Callback<User> {
                    override fun onResponse(req: Call<User>, res: Response<User>) {
                        val user = res.body()
                        repository.user.value = user
                        Log.d("SPLASH SCREEN", "onResponse: ${repository.user.value}")
                        if (user != null) {
                            if (callback != null) {
                                callback(user)
                            }
                        }
                    }

                    override fun onFailure(req: Call<User>, e: Throwable) {
                        Log.e("SPLASH SCREEN", "onFailure: $e")
                        GlobalScope.launch(Dispatchers.Main) {
                            delay(30000)
                            getUser(callback = callback)
                        }
                    }
                })
        }

        fun loadProfile(id: Long, callback: ((user: User) -> Unit)) {
            ApiRepository.getInstance().getSQL().getUserProfileByID(id)
                .enqueue(object : retrofit2.Callback<User> {
                    override fun onResponse(req: Call<User>, res: Response<User>) {
                        res.body()?.let { callback(it) }
                    }

                    override fun onFailure(req: Call<User>, e: Throwable) {
                        GlobalScope.launch(Dispatchers.Main) {
                            delay(30000)
                            loadProfile(id, callback)
                        }
                    }
                })
        }


        fun getPosts(userId: Long? = null, callback: (posts: List<Post>) -> Unit) {
            val apiNoSQL = ApiRepository.getInstance().getNoSQL()

            if (userId == null) {
                apiNoSQL.getPosts().enqueue(object : retrofit2.Callback<List<Post>> {
                    override fun onResponse(req: Call<List<Post>>, res: Response<List<Post>>) {
                        val posts = res.body()
                        if (posts != null) {
                            callback(posts)
                        }
                    }

                    override fun onFailure(req: Call<List<Post>>, e: Throwable) {
                        Log.e("SPLASH SCREEN", "onFailure: $e")
                        getPosts(null, callback)
                    }
                })
            } else {
                apiNoSQL.getPostsFromUser(userId).enqueue(object : retrofit2.Callback<List<Post>> {
                    override fun onResponse(req: Call<List<Post>>, res: Response<List<Post>>) {
                        val posts = res.body()
                        if (posts != null) {
                            callback(posts)
                        }
                    }

                    override fun onFailure(req: Call<List<Post>>, e: Throwable) {
                        Log.e("SPLASH SCREEN", "onFailure: $e")
                        getPosts(userId, callback)
                    }
                })
            }
        }



    }
}