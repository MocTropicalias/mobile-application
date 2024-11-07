package com.tropicalias.ui.profile.edit

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.tropicalias.adapter.ProfileAdapter
import com.tropicalias.api.model.User
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentEditProfileBinding
import com.tropicalias.utils.ApiHelper
import com.tropicalias.utils.MascotHelper
import retrofit2.Call
import retrofit2.Response
import java.util.UUID

class EditProfileViewModel : ViewModel() {

    val TAG: String = "EDIT PROFILE"
    private val repository = ApiRepository.getInstance()
    val user = repository.user
    val apiSQL = repository.getSQL()
    lateinit var binding: FragmentEditProfileBinding

    fun loadUser(adapter: ProfileAdapter) {
        binding.loading.visibility = View.VISIBLE
        binding.loading.setOnClickListener { }
        ApiHelper.getUser { user ->
            binding.loading.visibility = View.GONE
            adapter.imageUri = user.imageUri
            MascotHelper.getMascot(user.id!!) {
                adapter.color = it.colorScheme
                adapter.notifyDataSetChanged()
            }
            binding.nameEditText.setText(user.exibitionName)
            binding.usernameEditText.setText(user.username)
            binding.bioEditText.setText(user.userDescription)
        }
    }

    fun saveUpdates(name: String, username: String, bio: String, image: Uri?, con: Context) {

        user.value?.exibitionName = if (name != "") name else null
        user.value?.username = username
        user.value?.userDescription = if (bio != "") bio else null


        val storageRef =
            FirebaseStorage.getInstance().reference.child("user/${UUID.randomUUID()}.jpg")
        storageRef.putFile(image!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    downloadUri?.let { imageUri ->
                        user.value?.imageUri = imageUri

        user.value?.let { user ->
            apiSQL.updateUserProfile(user, user.id!!.toString())
                .enqueue(object : retrofit2.Callback<User> {
                    override fun onResponse(req: Call<User>, res: Response<User>) {
                        repository.user.value = res.body()
                        binding.loadingButton.visibility = View.GONE
                        binding.saveButton.text = "Salvar"
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
                photoUri = imageUri
            }
        )
                    }
                }
            }
    }

}