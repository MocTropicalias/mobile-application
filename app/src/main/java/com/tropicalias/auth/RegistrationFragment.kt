package com.tropicalias.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.tropicalias.R
import com.tropicalias.databinding.FragmentRegistrationBinding
import com.tropicalias.utils.Utils

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthenticationViewModel by activityViewModels()
    private lateinit var TAG: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        TAG = viewModel.TAG
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emailEditText.text = viewModel.email
        binding.passwordEditText.text = viewModel.password
        binding.usernameEditText.text = viewModel.username

        Utils.setValidDrawable(binding.usernameEditText, requireContext())
        Utils.setValidDrawable(binding.emailEditText, requireContext())
        Utils.setValidDrawable(binding.passwordEditText, requireContext())


        // Register
        binding.registerButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim().lowercase()
            val email = binding.emailEditText.text.toString().trim().lowercase()
            val password = binding.passwordEditText.text.toString()
            binding.loadingButton.visibility = View.VISIBLE
            binding.registerButton.text = ""

            Utils.checkInputsRegistration(
                username,
                email,
                password,
                binding,
                requireContext()
            ) { error ->
                if (!error) {
                    viewModel.username = binding.usernameEditText.text
                    viewModel.email = binding.emailEditText.text
                    viewModel.password = binding.passwordEditText.text


                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnFailureListener { e ->
                            if (e is FirebaseAuthUserCollisionException) {
                                Utils.setInvalidDrawable(binding.emailEditText, requireContext())
                                binding.emailErrorTextView.text = "Email already in use"
                            } else {
                                Log.e(TAG, "Error creating user: ${e.message}")
                            }
                        }
                        .addOnSuccessListener {
                            //                    Go to Profile Picture Selection
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, ProfilePictureFragment())
                                .addToBackStack(null)
                                .commit()
                            it.user?.sendEmailVerification()
                            viewModel.createUser(it.user)
                        }
                }
                binding.loadingButton.visibility = View.GONE
                binding.registerButton.text = "Cadastrar"
            }
        }

        // Change to Login
        binding.loginTextView.setOnClickListener {
            viewModel.username = binding.usernameEditText.text
            viewModel.email = binding.emailEditText.text
            viewModel.password = binding.passwordEditText.text

            // Navigate to the registration fragment
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
