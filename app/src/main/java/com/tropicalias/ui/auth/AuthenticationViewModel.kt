package com.tropicalias.ui.auth

import android.net.Uri
import android.text.Editable
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.tropicalias.api.api.ApiSQL
import com.tropicalias.api.model.User
import com.tropicalias.api.repository.ApiRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class AuthenticationViewModel : ViewModel() {

    var password: Editable? = null
    var email: Editable? = null
    var username: Editable? = null

    var imageUrl: Uri? = null

    val TAG = "AuthenticationLogging"

    private val sqlApi: ApiSQL = ApiRepository.getInstance().getSQL()

    fun uploadImageToFirebase() {
        val storageRef =
            FirebaseStorage.getInstance().reference.child("user/${UUID.randomUUID()}.jpg")
        storageRef.putFile(imageUrl!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    downloadUri?.let { imageUri ->
                        val user = FirebaseAuth.getInstance().currentUser
                        val profileUpdates = userProfileChangeRequest {
                            photoUri = imageUri
                        }
                        user?.let {
                            sqlApi.updateUserPhotoByFirebaseID(user.uid, imageUri.toString())
                                .enqueue(object : Callback<User> {
                                    override fun onResponse(req: Call<User>, res: Response<User>) {
                                        ApiRepository.getInstance().user.value = res.body()
                                    }

                                    override fun onFailure(req: Call<User>, e: Throwable) {
                                        uploadImageToFirebase()
                                    }
                                })
                            user.updateProfile(profileUpdates)
                                .addOnFailureListener {}
                        }
                    }

                }
            }
            .addOnFailureListener {}
    }

    fun createUser(user: FirebaseUser?) {
        val profileUpdates = userProfileChangeRequest {
            displayName = username.toString()
        }
        user?.let {
            val createUser = User(
                firebaseId = user.uid,
                username = username.toString(),
                email = email.toString(),
                senha = password.toString()
            )

            val call = sqlApi.createUser(createUser)

            call.enqueue(object : retrofit2.Callback<User> {
                override fun onResponse(call: Call<User>, res: Response<User>) {}

                override fun onFailure(call: Call<User>, e: Throwable) {}
            })


            user.updateProfile(profileUpdates)
                .addOnFailureListener {}
        }
    }

}