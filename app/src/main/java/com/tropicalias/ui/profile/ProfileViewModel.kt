package com.tropicalias.ui.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.tropicalias.api.model.User
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentProfileBinding
import retrofit2.Call
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    //    val apiNoSql = ApiRepository.getInstance().getNoSQL()
    val apiSQL = ApiRepository.getInstance().getSQL()

    var user: User? = null

    fun loadUserInformation(userProfile: User, binding: FragmentProfileBinding) {
        if (userProfile.nome != null) {
            binding.nameTextView.text = userProfile.nome
            binding.usernameTextView.text = "@${userProfile.username}"
        } else {
            binding.nameTextView.text = "@${userProfile.username}"
            binding.usernameTextView.text = ""
        }
        binding.descriptionTextView.text = userProfile.descricaoUsuario ?: ""


//        apiNoSql.getPostsFromUser(userProfile.id!!)
    }

    fun getUser(fbUser: FirebaseUser) {
        val call = apiSQL.getUserByFirebaseID(fbUser.uid)

        call.enqueue(object : retrofit2.Callback<User> {
            override fun onResponse(call: Call<User>, res: Response<User>) {
                val dbUser: User = res.body()!!
                user = dbUser
            }

            override fun onFailure(call: Call<User>, e: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

}