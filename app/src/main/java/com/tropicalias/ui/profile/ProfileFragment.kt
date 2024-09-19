package com.tropicalias.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.tropicalias.MainViewModel
import com.tropicalias.R.color
import com.tropicalias.adapter.ProfileAdapter
import com.tropicalias.databinding.FragmentProfileBinding
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


        // Settings
        binding.settingsButton.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        if (activityViewModel.userProfile != null) {
            viewModel.loadUserInformation(activityViewModel.userProfile!!, binding)
            activityViewModel.userProfile!!.urlFoto?.let { adapter.imageUrl = Uri.parse(it) }
        } else {
            viewModel.getUser(activityViewModel.user)
            viewModel.loadUserInformation(viewModel.user!!, binding)
            activityViewModel.user.photoUrl?.let { adapter.imageUrl = it }
        }

        // Profile Picture
        binding.viewPager.setAdapter(adapter)
        binding.viewPager.setCurrentItem(Int.MAX_VALUE / 2, false)
        val cardFlipPageTransformer = CardFlipPageTransformer2()
        cardFlipPageTransformer.isScalable = false
        binding.viewPager.setPageTransformer(cardFlipPageTransformer)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activityViewModel.userProfile = null
        viewModel.user = null
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), color.transparent)
        requireActivity().window.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    }
}