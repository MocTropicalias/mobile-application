package com.tropicalias.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tropicalias.R.color
import com.tropicalias.adapter.ProfileAdapter
import com.tropicalias.databinding.FragmentProfileBinding
import com.tropicalias.ui.profile.settings.SettingsActivity
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var adapter: ProfileAdapter
    private val viewModel: ProfileViewModel by viewModels()

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
        adapter = ProfileAdapter()
        adapter.imageURL = viewModel.user.photoUrl
        binding.viewPager.setAdapter(adapter)
        binding.viewPager.setCurrentItem(Int.MAX_VALUE / 2, false)
        val cardFlipPageTransformer = CardFlipPageTransformer2()
        cardFlipPageTransformer.isScalable = false
        binding.viewPager.setPageTransformer(cardFlipPageTransformer)


        // Settings
        binding.settingsButton.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), color.transparent)
        requireActivity().window.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    }
}