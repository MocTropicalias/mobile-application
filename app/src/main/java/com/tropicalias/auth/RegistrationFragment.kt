package com.tropicalias.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.tropicalias.R
import com.tropicalias.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthenticationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emailEditText.text = viewModel.email
        binding.passwordEditText.text = viewModel.password
        binding.usernameEditText.text = viewModel.username

        viewModel.setValidDrawable(binding.usernameEditText, requireContext())
        viewModel.setValidDrawable(binding.emailEditText, requireContext())
        viewModel.setValidDrawable(binding.passwordEditText, requireContext())


        // Register
        binding.registerButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

//            if (viewModel.checkInputs(username, email, password, binding, requireContext())) {
            viewModel.username = binding.usernameEditText.text
            viewModel.email = binding.emailEditText.text
            viewModel.password = binding.passwordEditText.text

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Erro ao se cadastrar", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnSuccessListener {
                    //Go to Profile Picture Selection
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ProfilePictureFragment())
                        .addToBackStack(null)
                        .commit()
                    it.user?.sendEmailVerification()
                    viewModel.createUser(it.user)
                }
//            }
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
