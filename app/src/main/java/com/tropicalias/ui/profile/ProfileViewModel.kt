package com.tropicalias.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.tropicalias.adapter.ProfileAdapter
import com.tropicalias.api.model.User
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    //    val apiNoSql = ApiRepository.getInstance().getNoSQL()
    val apiSQL = ApiRepository.getInstance().getSQL()
    lateinit var binding: FragmentProfileBinding
    lateinit var adapter: ProfileAdapter

    private val TAG = "ProfileLogging"


    fun loadProfile(id: Long) {
        apiSQL.getUserProfileByID(id).enqueue(object : retrofit2.Callback<User> {
            override fun onResponse(req: Call<User>, res: Response<User>) {
                res.body()?.let { setData(it) }
            }

            override fun onFailure(req: Call<User>, e: Throwable) {
                Log.e(TAG, "onFailure: $e")
                GlobalScope.launch(Dispatchers.Main) {
                    delay(30000)
                    loadProfile(id)
                }
            }
        })
    }

    fun setData(user: User) {
        // Name Display
        if (user.exibitionName == null) {
            binding.nameTextView.text = "@" + user.username
            binding.usernameTextView.text = ""
        } else {
            binding.nameTextView.text = user.exibitionName
            binding.usernameTextView.text = "@" + user.username
        }

        // Profile Picture
        adapter.imageUri = user.imageUri
        adapter.notifyDataSetChanged()

        // Bio
        binding.descriptionTextView.text = user.userDescription

        // Followers
        binding.followersTextView.text = "0"
        user.id?.let {
            apiSQL.getUserFollowersCount(it.toString()).enqueue(object : retrofit2.Callback<Int> {
                override fun onResponse(req: Call<Int>, res: Response<Int>) {
                    binding.followersTextView.text = res.body().toString()
                }

                override fun onFailure(req: Call<Int>, e: Throwable) {
                    Log.e(TAG, "onFailure: $e")
                }
            })
        }


    }

    fun followUser(idToFollow: Long) {

    }

}

