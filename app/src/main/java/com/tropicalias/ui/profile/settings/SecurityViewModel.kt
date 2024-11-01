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
    val apiSQL = repository.getSQL()

    fun loadUser(binding: FragmentSecurityBinding) {

        binding.loading.visibility = View.VISIBLE
        binding.loading.setOnClickListener { }
        ApiHelper.getUser { user ->
            binding.loading.visibility = View.GONE
            binding.emailEditText.setHint(formatEmail(user.email))
            binding.passwordEditText.setHint("******")


            binding.emailEditText.onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        if (binding.emailEditText.text.toString() == formatEmail(user.email)) {
                            binding.emailEditText.setText("")
                        }
                }
                }
            binding.passwordEditText.onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        binding.emailEditText.setText("")
                    }
            }
        }

    }

    private fun formatEmail(email: String): String {
        val split = email.split("@")
        val len = split[0].length

        return split[0].substring(0, 3) + "***" + split[0].substring(len - 3, len) + "@" + split[1]
    }

    fun saveUpdates(email: String?, password: String?, con: Context) {

        if (password != null) {
            FirebaseAuth.getInstance().currentUser?.updatePassword(password)
        }

        if (email != null) {
            user.value?.email = email
            user.value?.let { user ->
                apiSQL.updateUserProfile(user, user.id!!.toString())
                    .enqueue(object : retrofit2.Callback<User> {
                        override fun onResponse(req: Call<User>, res: Response<User>) {
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
        }
    }

    fun checkPassword(pass: String): String? {
        if (pass == user.value?.password) {
            return null
        }
        return null
    }

    fun checkEmail(email: String): String? {
        if (email == user.value?.email?.let { formatEmail(it) }) {
            return null
        }
        return email
    }
}