package com.tropicalias.ui.profile.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.tropicalias.AuthenticationActivity
import com.tropicalias.R
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentSettingsBinding
import com.tropicalias.utils.ApiHelper

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.securitySettingsButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SecurityFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.termsButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, UserTermsFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.supportButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SupportFragment())
                .addToBackStack(null)
                .commit()
        }


        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            ApiRepository.getInstance().user.value = null
            ApiRepository.getInstance().mascot.value = null
            val intent = Intent(requireContext(), AuthenticationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        ApiHelper.getUser { user ->
            if (user.userRole == "ADMIN") {
                binding.restrictedButton.visibility = View.VISIBLE
                binding.restrictedButton.setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, RestrictedAreaFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }


        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
