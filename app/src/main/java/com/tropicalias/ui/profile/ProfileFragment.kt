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
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentProfileBinding
import com.tropicalias.ui.profile.edit.EditProfileActivity
import com.tropicalias.ui.profile.settings.SettingsActivity
import com.tropicalias.utils.Utils
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(viewModel.TAG, "onViewCreated: PROFILE FRAGMENT")


        // Change status bar color
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            color.ciano
        )
        if (requireActivity().window != null) {
            requireActivity().window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        // Profile Picture
        adapter = ProfileAdapter(null, requireContext())
        binding.viewPager.setAdapter(adapter)
        binding.viewPager.setCurrentItem(Int.MAX_VALUE / 2, false)
        val cardFlipPageTransformer = CardFlipPageTransformer2()
        cardFlipPageTransformer.isScalable = false
        binding.viewPager.setPageTransformer(cardFlipPageTransformer)

        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            _binding?.viewPager?.setCurrentItem(Int.MAX_VALUE / 2 + 1, true)
            delay(1000)
            _binding?.viewPager?.setCurrentItem(Int.MAX_VALUE / 2 + 2, true)
        }

        Log.d("USER FRAGMENT", "onCreateView: ${ApiRepository.getInstance().user}")

        // Load Profile Information
        viewModel.binding = binding
        viewModel.adapter = adapter
        val userNotSelf: User? = arguments?.getParcelable("user_key")
        val repository = ApiRepository.getInstance()

        // Is it your profile?
        if (userNotSelf == null) {
            // It is my profile
            repository.user.observe(viewLifecycleOwner) { user ->
                user?.let {
                    viewModel.setData(user)
                }
            }


            if (repository.user.value == null) {
                val fbuser = FirebaseAuth.getInstance().currentUser
                viewModel.setData(
                    User(fbuser?.displayName!!, fbuser.photoUrl)
                )
                Utils.getUser {
                    viewModel.setData(it)
                }
            } else {
                viewModel.setData(repository.user.value!!)
            }


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
            // It is not my profile
            viewModel.loadProfile(userNotSelf.id!!)
            viewModel.setData(userNotSelf)


            binding.settingsButton.visibility = View.GONE
            binding.editProfileButton.visibility = View.GONE

            // Follow
            binding.followButton.visibility = View.VISIBLE
            binding.followButton.setOnClickListener {
                userNotSelf.id.let {
                    viewModel.followUser(it)
                }
            }
        }


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