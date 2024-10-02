package com.tropicalias.auth

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.text.Editable
import android.util.Log
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.tropicalias.R
import com.tropicalias.api.api.ApiSQL
import com.tropicalias.api.model.User
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentRegistrationBinding
import retrofit2.Call
import retrofit2.Response
import java.util.UUID

class AuthenticationViewModel : ViewModel() {

    var password: Editable? = null
    var email: Editable? = null
    var username: Editable? = null

    val imageUrl = MutableLiveData<Uri>()

    val TAG = "AuthenticationLogging"

    private val sqlApi: ApiSQL = ApiRepository.getInstance().getSQL()

    // Input Validation

    private fun isValidEmail(email: String): String? {
        if (email.length < 5 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Email inválido"
        }
        return null
    }

    private fun isValidUsername(username: String): String? {

        // Check if username already exists
        var usernameInUse = false
        sqlApi.getAllUsers().enqueue(object : retrofit2.Callback<List<User>> {
            override fun onResponse(req: Call<List<User>>, res: Response<List<User>>) {
                val users = res.body()
                if (users != null) {
                    if (users.any { it.username == username }) {
                        usernameInUse = true
                    }
                }
            }

            override fun onFailure(req: Call<List<User>>, e: Throwable) {
                Log.e(TAG, "onFailure: $e")
            }
        })

        if (usernameInUse) {
            return "Nome de usuário ja existe"
        }


        if (username.length <= 4 || username.length >= 20) {
            return "Nome de usuário muito curto"
        }
        return null
    }

    private fun isValidPassword(password: String): String? {
        if (password.length < 4) {
            return "\nSenha muito curta"
        }
        if (password.length > 100) {
            return "\nSenha muito longa"
        }
        return null
    }


    fun checkInputs(
        username: String,
        email: String,
        password: String,
        binding: FragmentRegistrationBinding,
        context: Context
    ): Boolean {
        // Reseting error messages
        binding.errorTextView.text = ""
        setValidDrawable(binding.usernameEditText, context)
        setValidDrawable(binding.emailEditText, context)
        setValidDrawable(binding.passwordEditText, context)

        //Variable initialization
        var hasError = false
        var errors = ""
        var erro: String? = null

        // Validations
        // Username
        erro = isValidUsername(username)
        if (erro != null) {
            errors += erro + "\n\n"
            setInvalidDrawable(binding.usernameEditText, context)
            hasError = true
        }
        // Email
        erro = isValidEmail(email)
        if (erro != null) {
            errors += erro + "\n\n"
            setInvalidDrawable(binding.emailEditText, context)
            hasError = true
        }
        // Password
        erro = isValidPassword(password)
        if (erro != null) {
            errors += erro + "\n\n"
            setInvalidDrawable(binding.passwordEditText, context)
            hasError = true
        }
        binding.errorTextView.text = errors
        return !hasError
    }


    //Handle Drawable colors

    fun setInvalidDrawable(editText: EditText, context: Context) {
        (editText.background as GradientDrawable).setStroke(
            dpToPx(3, context),
            ContextCompat.getColor(context, R.color.vermelho)
        )
        editText.compoundDrawables[0]?.mutate()?.setTint(
            ContextCompat.getColor(context, R.color.vermelho)
        )
    }

    fun setValidDrawable(editText: EditText, context: Context) {
        (editText.background as GradientDrawable).setStroke(
            dpToPx(3, context),
            ContextCompat.getColor(context, R.color.black)
        )
        editText.compoundDrawables[0]?.mutate()?.setTint(
            ContextCompat.getColor(context, R.color.black)
        )
    }


    // Handle image

    fun uploadImageToFirebase(uri: Uri) {
        val storageRef =
            FirebaseStorage.getInstance().reference.child("images/${UUID.randomUUID()}.jpg")
        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    imageUrl.value = downloadUri
                }
            }
            .addOnFailureListener {}
    }

    private fun dpToPx(dp: Int, context: Context): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    // Registering
    fun storeImageUri() {
        imageUrl.value?.let { imageUri ->
            val user = FirebaseAuth.getInstance().currentUser
            val profileUpdates = userProfileChangeRequest {
                photoUri = imageUri
            }
            uploadImageToFirebase(imageUri)
            user?.let {
                sqlApi.updateUserPhotoByFirebaseID(user.uid, imageUri.toString())
                user.updateProfile(profileUpdates)
                    .addOnFailureListener {}
            }
        }
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