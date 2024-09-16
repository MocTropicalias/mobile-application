package com.tropicalias.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.tropicalias.MainActivity
import com.tropicalias.R
import com.tropicalias.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthenticationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emailEditText.text = viewModel.email
        binding.passwordEditText.text = viewModel.password

        viewModel.setValidDrawable(binding.emailEditText, requireContext())
        viewModel.setValidDrawable(binding.passwordEditText, requireContext())


        // Login
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty()) {
                viewModel.setInvalidDrawable(binding.emailEditText, requireContext())
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                viewModel.setInvalidDrawable(binding.passwordEditText, requireContext())
                return@setOnClickListener
            }

            //Firebase login
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(activity, MainActivity::class.java))
                        activity?.finish()
                    } else {
                        binding.errorTextView.text = "Email ou senha incorretos"
                        viewModel.setInvalidDrawable(binding.passwordEditText, requireContext())
                        viewModel.setInvalidDrawable(binding.emailEditText, requireContext())
                    }
                }
        }

        // Change to Register
        binding.registerTextView.setOnClickListener {
            // Store the email and password in ViewModel
            viewModel.email = binding.emailEditText.text
            viewModel.password = binding.passwordEditText.text

            // Temporarily clear the text in email and password fields
            binding.emailEditText.text = null
            binding.passwordEditText.text = null

            // Navigate to the registration fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegistrationFragment())
                .addToBackStack(null)
                .commit()

            binding.emailEditText.text = viewModel.email
            binding.passwordEditText.text = viewModel.password
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
