package com.tropicalias.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tropicalias.adapter.ProfileAdapter
import com.tropicalias.api.model.User
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentProfileBinding
import retrofit2.Call
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    //    val apiNoSql = ApiRepository.getInstance().getNoSQL()
    val apiSQL = ApiRepository.getInstance().getSQL()
    lateinit var binding: FragmentProfileBinding
    lateinit var adapter: ProfileAdapter

    private val TAG = "ProfileLogging"


    val user = MutableLiveData<User>()

    fun loadProfile(uid: String) {
        apiSQL.getUserByFirebaseID(uid).enqueue(object : retrofit2.Callback<User> {
            override fun onResponse(req: Call<User>, res: Response<User>) {
                user.value = res.body()
            }

            override fun onFailure(req: Call<User>, e: Throwable) {
                Log.e(TAG, "onFailure: $e")
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
        adapter.imageUrl = user.photoUrl
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