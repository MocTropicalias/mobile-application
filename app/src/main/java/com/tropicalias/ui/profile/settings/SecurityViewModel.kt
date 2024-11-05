package com.tropicalias.ui.profile.settings

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.tropicalias.api.model.User
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentSecurityBinding
import com.tropicalias.utils.ApiHelper
import retrofit2.Call
import retrofit2.Response

class SecurityViewModel : ViewModel() {

    private val repository = ApiRepository.getInstance()
    val user = repository.user

    fun loadUser(binding: FragmentSecurityBinding) {
        binding.loading.visibility = View.VISIBLE

        ApiHelper.getUser { user ->
            binding.loading.visibility = View.GONE
            binding.emailEditText.setHint(formatEmail(FirebaseAuth.getInstance().currentUser?.email))
            binding.passwordEditText.setHint("******")
        }

    }

    private fun formatEmail(email: String?): String {
        if (email == null) {
            return ""
        }

        val split = email.split("@")
        val len = split[0].length
        return split[0].substring(0, 2) + "***" + split[0].substring(len - 2, len) + "@" + split[1]
    }

    fun saveUpdates(email: String, password: String, con: Context) {

        ApiHelper.getUser { user ->
            if (password != "") {
                FirebaseAuth.getInstance().currentUser?.updatePassword(password)
                user.email = password
            }

            if (email != "") {
                user.email = email
                ApiRepository.getInstance().getSQL().updateUserProfile(user, user.id!!.toString())
                    .enqueue(object : retrofit2.Callback<User> {
                        override fun onResponse(req: Call<User>, res: Response<User>) {
                            ApiRepository.getInstance().user.value = res.body()
                        }

                        override fun onFailure(req: Call<User>, t: Throwable) {
                            Toast.makeText(
                                con,
                                "Erro ao atualizar: tente novamente mais tarde",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                FirebaseAuth.getInstance().currentUser?.verifyBeforeUpdateEmail(email)
            }
        }
    }
}