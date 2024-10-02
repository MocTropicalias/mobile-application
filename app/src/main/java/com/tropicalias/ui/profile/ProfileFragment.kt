package com.tropicalias.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.tropicalias.MainViewModel
import com.tropicalias.R.color
import com.tropicalias.adapter.ProfileAdapter
import com.tropicalias.api.model.User
import com.tropicalias.databinding.FragmentProfileBinding
import com.tropicalias.ui.profile.edit.EditProfileActivity
import com.tropicalias.ui.profile.settings.SettingsActivity
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var adapter: ProfileAdapter
    private val viewModel: ProfileViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // Change status bar color
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), color.ciano)
        requireActivity().window.insetsController?.setSystemBarsAppearance(
            0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )

        // Profile Picture
        adapter = ProfileAdapter(null)
        binding.viewPager.setAdapter(adapter)
        binding.viewPager.setCurrentItem(Int.MAX_VALUE / 2, false)
        val cardFlipPageTransformer = CardFlipPageTransformer2()
        cardFlipPageTransformer.isScalable = false
        binding.viewPager.setPageTransformer(cardFlipPageTransformer)


        // Load Profile Information
        viewModel.binding = binding
        viewModel.adapter = adapter
        val user: User? = arguments?.getParcelable("user_key", User::class.java)
        val yourUser = FirebaseAuth.getInstance().currentUser!!
        Log.d("USUARIO FIREBASE", "onCreateView: $yourUser")
        viewModel.loadProfile(user?.firebaseId ?: yourUser.uid)
        viewModel.setData(
            user ?: User(
                yourUser.displayName!!,
                yourUser.photoUrl,
            )
        )
        viewModel.user.observe(viewLifecycleOwner) {
            viewModel.setData(it)
        }

        // Is it your profile?
        if (user == null) {
            // It is
            // Settings
            binding.settingsButton.visibility = View.VISIBLE
            binding.settingsButton.setOnClickListener {
                startActivity(Intent(requireContext(), SettingsActivity::class.java))
            }

            // Edit Profile
            binding.editProfileButton.visibility = View.VISIBLE
            binding.editProfileButton.setOnClickListener {
                startActivity(Intent(requireContext(), EditProfileActivity::class.java))
            }

            binding.followButton.visibility = View.GONE
        } else {
            // It is not
            binding.settingsButton.visibility = View.GONE
            binding.editProfileButton.visibility = View.GONE

            // Follow
            binding.followButton.visibility = View.VISIBLE
            binding.followButton.setOnClickListener {
                user.id?.let {
                    viewModel.followUser(it)
                }
            }
        }





        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null


        // Change status bar color
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), color.transparent)
        requireActivity().window.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    }
}