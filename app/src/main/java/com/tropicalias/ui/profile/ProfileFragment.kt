package com.tropicalias.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tropicalias.adapter.ProfileAdapter
import com.tropicalias.databinding.FragmentProfileBinding
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


        // Get ViewPager2 and Set Adapter
        adapter = ProfileAdapter()
        adapter.imageURL = viewModel.user.photoUrl
        binding.viewPager.setAdapter(adapter)
        binding.viewPager.setCurrentItem(Int.MAX_VALUE / 2, false)
        // Create an object of page transformer
        val cardFlipPageTransformer = CardFlipPageTransformer2()
        cardFlipPageTransformer.isScalable = false
        // Assign the page transformer to the ViewPager2.
        binding.viewPager.setPageTransformer(cardFlipPageTransformer)


        // Settings
        binding.settingsButton.setOnClickListener {
//            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}