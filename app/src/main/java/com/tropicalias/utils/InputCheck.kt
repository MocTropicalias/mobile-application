package com.tropicalias.utils

import android.content.Context
import com.tropicalias.api.model.User
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentEditProfileBinding
import com.tropicalias.databinding.FragmentRegistrationBinding
import com.tropicalias.databinding.FragmentSecurityBinding
import com.tropicalias.utils.DrawableHandler.Companion.setInvalidDrawable
import com.tropicalias.utils.DrawableHandler.Companion.setValidDrawable
import retrofit2.Call
import retrofit2.Response
import java.util.regex.Pattern

class InputCheck {
    companion object{

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

            return erro
        }

        private fun isValidPassword(password: String): String? {
            if (password.length < 6) {
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
            ApiRepository.getInstance().getSQL().getAllUsers().enqueue(object : retrofit2.Callback<List<User>> {
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



    }
}