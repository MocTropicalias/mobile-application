package com.tropicalias

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.tropicalias.api.api.ApiSQL
import com.tropicalias.api.model.User
import com.tropicalias.api.repository.ApiRepository
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {
    val firebaseUser = FirebaseAuth.getInstance().currentUser!!
    private val apiSQL: ApiSQL = ApiRepository.getInstance().getSQL()
    var userProfile: User? = null
    var clickUserProfile: User? = null

    fun getUser(callback: (user: User) -> Unit) {
        val call = apiSQL.getUserByFirebaseID(firebaseUser.uid)

        call.enqueue(object : retrofit2.Callback<User> {
            override fun onResponse(call: Call<User>, res: Response<User>) {
                val dbUser: User? = res.body()

                Log.d("TAGProfile", "Request URL: " + call.request().url())
                Log.d("TAGProfile", "Request headers: " + call.request().headers())
                Log.d("TAGProfile", "Response: $res")
                Log.d("TAGProfile", "Response Body: ${res.body()}")


                Log.d("TAGProfile", "onResponse: $dbUser")
                dbUser?.let {
                    callback(dbUser)
                }

            }

            override fun onFailure(call: Call<User>, e: Throwable) {
                Log.d("TAGProfile", "onFailure: $e")
                Log.d("TAGProfile", "Request URL: " + call.request().url())
                Log.d("TAGProfile", "Request headers: " + call.request().headers())
            }

        })
    }

}