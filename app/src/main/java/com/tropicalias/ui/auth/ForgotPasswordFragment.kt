package com.tropicalias.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.tropicalias.R
import com.tropicalias.databinding.FragmentForgotPasswordBinding
import com.tropicalias.utils.InputCheck

class ForgotPasswordFragment : Fragment() {


    private val binding: FragmentForgotPasswordBinding get() = _binding!!
    private var _binding: FragmentForgotPasswordBinding? = null
    private val viewModel: AuthenticationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emailEditText.text = viewModel.email

        binding.backButton.setOnClickListener {
            viewModel.email = binding.emailEditText.text

            requireActivity().onBackPressed()
        }

        binding.sendEmailButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            InputCheck.checkInputsResetPassword(email, binding, requireContext()) { error ->
                if (!error) {
                    viewModel.email = binding.emailEditText.text
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    Toast.makeText(
                        requireContext(),
                        "Email de recuperação enviado",
                        Toast.LENGTH_SHORT
                    ).show()
                    requireActivity().onBackPressed()
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}