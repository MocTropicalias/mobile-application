package com.tropicalias.ui.profile

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.tropicalias.R
import com.tropicalias.adapter.PostAdapter
import com.tropicalias.adapter.ProfileAdapter
import com.tropicalias.api.model.Follow
import com.tropicalias.api.model.User
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentProfileBinding
import com.tropicalias.utils.ApiHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    private val apiNoSql = ApiRepository.getInstance().getNoSQL()
    private val apiSQL = ApiRepository.getInstance().getSQL()
    lateinit var binding: FragmentProfileBinding
    lateinit var adapter: ProfileAdapter
    lateinit var postAdapter: PostAdapter

    val TAG = "ProfileLogging"

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
        binding.followersTextView.text = (user.followersCount ?: "0").toString()
        user.id?.let {
            ApiHelper.getUser { myuser ->
                apiSQL.getFollowUser(user.id, myuser.id!!).enqueue(object : Callback<Follow> {
                    override fun onResponse(req: Call<Follow>, res: Response<Follow>) {
                        binding.followButton.setBackgroundResource(if (res.body()?.following == true) R.drawable.bg_button_deactivated else R.drawable.bg_button)
                        binding.followButton.text =
                            (if (res.body()?.following == true) "Deixar de seguir" else "Seguir")
                    }

                    override fun onFailure(req: Call<Follow>, e: Throwable) {}
                })
            }

            binding.loading.visibility = View.VISIBLE
            ApiHelper.getPosts(it) { posts ->
                postAdapter.posts = posts
                postAdapter.notifyDataSetChanged()
                binding.loading.visibility = View.GONE
            }

        }


    }

    fun followUser(idToFollow: Long) {
        ApiHelper.getUser { user ->
            apiSQL.toggleFollowUser(idToFollow, user.id!!).enqueue(object : Callback<Follow> {
                override fun onResponse(req: Call<Follow>, res: Response<Follow>) {
                    apiSQL.getUserFollowersCount(idToFollow.toString())
                        .enqueue(object : Callback<Int> {
                            override fun onResponse(req: Call<Int>, res: Response<Int>) {
                                binding.followersTextView.text = res.body().toString()
                            }

                            override fun onFailure(req: Call<Int>, e: Throwable) {
                                Log.e(TAG, "onFailure: $e")
                            }
                        })
                    binding.followButton.setBackgroundResource(if (res.body()?.following == true) R.drawable.bg_button_deactivated else R.drawable.bg_button)
                    binding.followButton.text =
                        (if (res.body()?.following == true) "Deixar de seguir" else "Seguir")
                }

                override fun onFailure(req: Call<Follow>, e: Throwable) {
                    Log.e(TAG, "onFailure: $e")
                }
            })
        }
    }

}

