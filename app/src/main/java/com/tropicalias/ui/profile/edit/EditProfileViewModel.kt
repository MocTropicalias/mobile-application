package com.tropicalias.ui.profile.edit

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.tropicalias.adapter.ProfileAdapter
import com.tropicalias.api.model.User
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentEditProfileBinding
import com.tropicalias.utils.ApiHelper
import retrofit2.Call
import retrofit2.Response

class EditProfileViewModel : ViewModel() {

    private val repository = ApiRepository.getInstance()
    val user = repository.user
    val apiSQL = repository.getSQL()

    fun loadUser(binding: FragmentEditProfileBinding, adapter: ProfileAdapter) {
        binding.loading.visibility = View.VISIBLE
        binding.loading.setOnClickListener { }
        ApiHelper.getUser { user ->
            binding.loading.visibility = View.GONE
            adapter.imageUri = user.imageUri
            adapter.notifyDataSetChanged()
            binding.nameEditText.setText(user.exibitionName)
            binding.usernameEditText.setText(user.username)
            binding.bioEditText.setText(user.userDescription)
        }
    }

    fun saveUpdates(name: String, username: String, bio: String, image: Uri?, con: Context) {

        user.value?.exibitionName = name
        user.value?.username = username
        user.value?.userDescription = bio
        user.value?.imageUri = image

        user.value?.let { user ->
            Log.d("EDIT PROFILE", "saveUpdates: $user")
            apiSQL.updateUserProfile(user, user.id!!.toString())
                .enqueue(object : retrofit2.Callback<User> {
                    override fun onResponse(req: Call<User>, res: Response<User>) {
                        Log.d("EDIT PROFILE", "onResponse: ${ApiHelper.bodyToString(req.request().body)}")
                        repository.user.value = res.body()
                    }

                    override fun onFailure(req: Call<User>, t: Throwable) {
                        Toast.makeText(
                            con,
                            "Erro ao atualizar: tente novamente mais tarde",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
        FirebaseAuth.getInstance().currentUser?.updateProfile(
            userProfileChangeRequest {
                displayName = username
                photoUri = image
            }
        )


    }

}