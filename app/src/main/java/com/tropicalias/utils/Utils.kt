package com.tropicalias.utils

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.tropicalias.R
import com.tropicalias.api.model.User
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentEditProfileBinding
import com.tropicalias.databinding.FragmentRegistrationBinding
import com.tropicalias.databinding.FragmentSecurityBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.UUID
import java.util.regex.Pattern

class Utils {
    companion object {

        private val sqlApi = ApiRepository.getInstance().getSQL()

        fun getChoserIntent(context: Context): Pair<Intent, Uri> {
            val galleryIntent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            val imageFile = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "profile_picture_${UUID.randomUUID()}.jpg"
            )
            val imageUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            }
            val chooserIntent =
                Intent.createChooser(galleryIntent, "Selecione sua foto de perfil").apply {
                    putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
                }
            return Pair(chooserIntent, imageUri)
        }


        // Input Validation
        private fun isValidEmail(email: String): String? {
            if (email.length < 5 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return "Email inválido"
            }
            return null
        }

        private fun isValidUsername(username: String): String? {

            var erro: String? = null

            if (username.length <= 2) {
                erro = "Nome de usuário muito curto"
            }
            if (username.length >= 20) {
                erro = "Nome de usuário muito longo"
            }
            if (!Pattern.matches("[a-zA-Z0-9]+", username)) {
                erro = "Nome de usuário contem caracteres inválidos"
            }

            // Check if username already exists

            return null
        }

        private fun isValidPassword(password: String): String? {
            if (password.length < 4) {
                return "Senha muito curta"
            }
            if (password.length > 100) {
                return "Senha muito longa"
            }
            return null
        }

        private fun isValidName(name: String): String? {
            return if (name.length > 60) "Nome muito longo" else null
        }

        private fun isValidBio(bio: String): String? {
            return if (bio.length > 60) "Descrição muito longa" else null
        }

        private fun isNameAvailable(
            username: String,
            callback: ((usernameInUse: Boolean) -> Unit)? = null
        ) {
            var usernameInUse = false
            sqlApi.getAllUsers().enqueue(object : retrofit2.Callback<List<User>> {
                override fun onResponse(req: Call<List<User>>, res: Response<List<User>>) {
                    val users = res.body()
                    if (users != null) {
                        if (users.any { it.username == username && it.username != ApiRepository.getInstance().user.value?.username }) {
                            usernameInUse = true
                        }
                    }
                    if (callback != null) {
                        callback(usernameInUse)
                    }
                }

                override fun onFailure(req: Call<List<User>>, e: Throwable) {
                    Log.e("GET ALL USERS", "onFailure: $e")
                    isNameAvailable(username, callback)
                }
            })
        }

        fun checkInputsRegistration(
            username: String,
            email: String,
            password: String,
            binding: FragmentRegistrationBinding,
            context: Context,
            callback: (hasError: Boolean) -> Unit
        ) {
            // Reseting error messages
            binding.usernameErrorTextView.text = ""
            binding.emailErrorTextView.text = ""
            binding.passwordErrorTextView.text = ""
            setValidDrawable(binding.usernameEditText, context)
            setValidDrawable(binding.emailEditText, context)
            setValidDrawable(binding.passwordEditText, context)

            //Variable initialization
            var hasError = false
            var erro: String? = null

            // Validations
            // Username
            erro = isValidUsername(username)
            if (erro != null) {
                binding.usernameErrorTextView.text = erro
                setInvalidDrawable(binding.usernameEditText, context)
                hasError = true
            }
            // Email
            erro = isValidEmail(email)
            if (erro != null) {
                binding.emailErrorTextView.text = erro
                setInvalidDrawable(binding.emailEditText, context)
                hasError = true
            }
            // Password
            erro = isValidPassword(password)
            if (erro != null) {
                binding.passwordErrorTextView.text = erro
                setInvalidDrawable(binding.passwordEditText, context)
                hasError = true
            }
            isNameAvailable(username) { usernameInUse ->
                if (usernameInUse) {
                    binding.usernameErrorTextView.text = "Nome de usuário em uso"
                    setInvalidDrawable(binding.usernameEditText, context)
                    hasError = true
                }
                callback(hasError)
            }
        }

        fun checkInputsEditProfile(
            name: String,
            username: String,
            bio: String,
            binding: FragmentEditProfileBinding,
            context: Context,
            callback: ((hasError: Boolean) -> Unit)
        ) {
            // Reseting error messages
            binding.usernameErrorTextView.text = ""
            binding.nameErrorTextView.text = ""
            binding.bioErrorTextView.text = ""
            setValidDrawable(binding.usernameEditText, context)
            setValidDrawable(binding.nameEditText, context)
            setValidDrawable(binding.bioEditText, context)

            //Variable initialization
            var hasError = false
            var erro: String? = null

            // Validations
            // Username
            erro = isValidUsername(username)
            if (erro != null) {
                binding.usernameErrorTextView.text = erro
                setInvalidDrawable(binding.usernameEditText, context)
                hasError = true
            }
            // Name
            erro = isValidName(name)
            if (erro != null) {
                binding.nameErrorTextView.text = erro
                setInvalidDrawable(binding.nameEditText, context)
                hasError = true
        }
            // Name
            erro = isValidBio(bio)
            if (erro != null) {
                binding.bioErrorTextView.text = erro
                setInvalidDrawable(binding.nameEditText, context)
                hasError = true
            }

            isNameAvailable(username) { usernameInUse ->
                if (usernameInUse) {
                    binding.usernameErrorTextView.text = "Nome de usuário em uso"
                    setInvalidDrawable(binding.usernameEditText, context)
                    hasError = true
                }
                callback(hasError)
            }
        }

        fun checkInputsEditSecurity(
            email: String?,
            password: String?,
            binding: FragmentSecurityBinding,
            context: Context,
            callback: (hasError: Boolean) -> Unit
        ) {
            // Reseting error messages
            binding.emailErrorTextView.text = ""
            binding.passwordErrorTextView.text = ""
            setValidDrawable(binding.emailEditText, context)
            setValidDrawable(binding.passwordEditText, context)

            //Variable initialization
            var hasError = false
            var erro: String? = null

            // Validations
            // Email
            if (email != null) {
                erro = isValidEmail(email)
                if (erro != null) {
                    binding.emailErrorTextView.text = erro
                    setInvalidDrawable(binding.emailEditText, context)
                    hasError = true
                }
            }
            // Password
            if (password != null) {
                erro = isValidPassword(password)
                if (erro != null) {
                    binding.passwordErrorTextView.text = erro
                    setInvalidDrawable(binding.passwordEditText, context)
                    hasError = true
                }
            }
            callback(hasError)

        }


        //Handle Drawable colors
        fun shakeAnimation(view: View) {
            val animator = ObjectAnimator.ofFloat(
                view,
                "translationX",
                *floatArrayOf(0F, 25F, -25F, 25F, -25F, 15F, -15F, 6F, -6F, 0F)
            );
            animator.setDuration(500); // Duração da animação
            animator.start(); // Inicia a animação
        }

        fun setInvalidDrawable(editText: EditText, context: Context) {
            (editText.background as GradientDrawable).setStroke(
                dpToPx(3, context),
                ContextCompat.getColor(context, R.color.vermelho)
            )
            editText.compoundDrawables[0]?.mutate()?.setTint(
                ContextCompat.getColor(context, R.color.vermelho)
            )
            shakeAnimation(editText)
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

        private fun dpToPx(dp: Int, context: Context): Int {
            return (dp * context.resources.displayMetrics.density).toInt()
        }

        //User
        fun getUser(callback: ((user: User) -> Unit)? = null) {
            val repository = ApiRepository.getInstance()
            repository.user.value?.let { user ->
                if (callback != null) {
                    callback(user)
                }
            }

            repository.getSQL().getUserByFirebaseID(FirebaseAuth.getInstance().currentUser?.uid!!)
                .enqueue(object : retrofit2.Callback<User> {
                    override fun onResponse(req: Call<User>, res: Response<User>) {
                        val user = res.body()
                        repository.user.value = user
                        Log.d("SPLASH SCREEN", "onResponse: ${repository.user.value}")
                        if (user != null) {
                            repository.getSQL().getUserProfileByID(user.id!!)
                                .enqueue(object : retrofit2.Callback<User> {
                                    override fun onResponse(req: Call<User>, res: Response<User>) {
                                        val user = res.body()
                                        repository.user.value?.followersCount = user?.followersCount
                                        if (callback != null) {
                                            if (user != null) {
                                                callback(user)
                                            }
                                        }
                                    }

                                    override fun onFailure(p0: Call<User>, p1: Throwable) {
                                        TODO("Not yet implemented")
                                    }
                                })
                        }
                    }

                    override fun onFailure(req: Call<User>, e: Throwable) {
                        Log.e("SPLASH SCREEN", "onFailure: $e")
                        GlobalScope.launch(Dispatchers.Main) {
                            delay(30000)
                            getUser(callback = callback)
                        }
                    }
                })
        }


        //Helper API
        fun bodyToString(requestBody: RequestBody?): String {
            return try {
                val buffer = Buffer()
                requestBody?.writeTo(buffer)
                buffer.readString(StandardCharsets.UTF_8)
            } catch (e: Exception) {
                "Unable to log request body"
            }
        }


    }
}