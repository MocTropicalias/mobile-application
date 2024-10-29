package com.tropicalias.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.tropicalias.MainViewModel
import com.tropicalias.R.color
import com.tropicalias.adapter.PostAdapter
import com.tropicalias.adapter.ProfileAdapter
import com.tropicalias.api.model.User
import com.tropicalias.databinding.FragmentProfileBinding
import com.tropicalias.ui.posts.newpost.NewPostActivity
import com.tropicalias.ui.profile.edit.EditProfileActivity
import com.tropicalias.ui.profile.settings.SettingsActivity
import com.tropicalias.utils.ApiHelper
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
        // Change status bar color
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            color.ciano
        )
        requireActivity().window.insetsController?.setSystemBarsAppearance(
            0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
        viewModel.postAdapter = PostAdapter(emptyList())

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = viewModel.postAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager

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

        // Load Profile Information
        viewModel.binding = binding
        viewModel.adapter = adapter
        val userNotSelfId = arguments?.getLong("userId")
        binding.loading.visibility = View.VISIBLE
        loadUser(userNotSelfId)

        // Is it your profile?
        ApiHelper.getUser { user ->
            if (userNotSelfId != null && userNotSelfId != user.id) {
                binding.settingsButton.visibility = View.GONE
                binding.editProfileButton.visibility = View.GONE

                // Follow
                binding.followButton.visibility = View.VISIBLE
                binding.followButton.setOnClickListener {
                    userNotSelfId.let {
                        viewModel.followUser(it)
                    }
                }
            } else {
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
            }
        }


        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(requireContext(), NewPostActivity::class.java))
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.loading.visibility = View.VISIBLE
            loadUser(userNotSelfId)
            binding.swipeRefreshLayout.isRefreshing = false
        }



    }

    private fun loadUser(
        userNotSelfId: Long?
    ) {
        if (userNotSelfId != null) {
            ApiHelper.loadProfile(userNotSelfId) {
                viewModel.setData(it)
            }
        } else {
            // It is my profile
            val fbuser = FirebaseAuth.getInstance().currentUser
            viewModel.setData(
                User(fbuser?.displayName!!, fbuser.photoUrl)
            )
            ApiHelper.getUser {
                viewModel.setData(it)
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